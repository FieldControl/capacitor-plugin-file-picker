import Foundation
import Capacitor
import MobileCoreServices

struct PickerSettings {
    let multiple: Bool
    let limit: Int
    let extUTIs: [String]

    static func from(_ call: CAPPluginCall, mimeOverride: [String]? = nil) -> PickerSettings {
        let multiple = call.getBool("multiple") ?? false
        let limit = call.getInt("limit") ?? 0
        let mimes = mimeOverride ?? call.getArray("mimes", String.self) ?? ["*"]
        var extUTIs = [String]()

        for mime in mimes {
            var uti: CFString = kUTTypeData
            if mime.hasPrefix("image/") {
                uti = kUTTypeImage
            } else if mime.hasPrefix("video/") {
                uti = kUTTypeMovie
            } else if mime.hasPrefix("audio/") {
                uti = kUTTypeAudio
            } else if mime.hasPrefix("text/") {
                uti = kUTTypeText
            } else if mime.contains("*") {
                uti = kUTTypeData
            } else if let converted = UTTypeCreatePreferredIdentifierForTag(
                kUTTagClassMIMEType,
                mime as CFString,
                nil)?.takeRetainedValue() {
                uti = converted
            }
            extUTIs.append(uti as String)
        }

        return PickerSettings(multiple: multiple, limit: limit, extUTIs: extUTIs)
    }
}

@objc(FilePickerPlugin)
public class FilePickerPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "FilePicker"
    public let jsName = "FilePicker"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "pick", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "pickImages", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "pickVideos", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "pickFiles", returnType: CAPPluginReturnPromise)
    ]

    private var savedCall: CAPPluginCall?
    private var settings: PickerSettings?

    @objc func pick(_ call: CAPPluginCall) {
        openPicker(call: call)
    }

    @objc func pickImages(_ call: CAPPluginCall) {
        openPicker(call: call, mimeOverride: ["image/*"] )
    }

    @objc func pickVideos(_ call: CAPPluginCall) {
        openPicker(call: call, mimeOverride: ["video/*"] )
    }

    @objc func pickFiles(_ call: CAPPluginCall) {
        openPicker(call: call, mimeOverride: ["*/*"] )
    }

    private func openPicker(call: CAPPluginCall, mimeOverride: [String]? = nil) {
        self.savedCall = call
        let s = PickerSettings.from(call, mimeOverride: mimeOverride)
        self.settings = s
        DispatchQueue.main.async {
            if s.extUTIs.count == 1 && s.extUTIs.first == (kUTTypeMovie as String) {
                let picker = UIImagePickerController()
                picker.delegate = self
                picker.mediaTypes = [kUTTypeMovie as String]
                picker.allowsEditing = false
                picker.sourceType = .photoLibrary
                self.bridge?.viewController?.present(picker, animated: true)
            } else {
                let docPicker = UIDocumentPickerViewController(documentTypes: s.extUTIs, in: .import)
                docPicker.delegate = self
                docPicker.allowsMultipleSelection = s.multiple
                docPicker.modalPresentationStyle = .formSheet
                self.bridge?.viewController?.present(docPicker, animated: true)
            }
        }
    }
}

extension FilePickerPlugin: UIDocumentPickerDelegate {
    public func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        guard let s = settings else { return }
        var fileArray = [JSObject]()

        if s.multiple && s.limit > 0 && urls.count > s.limit {
            let excess = urls.count - s.limit
            for _ in 0..<excess {
                urls.removeLast()
            }
        }

        for url in urls {
            var js = JSObject()
            js["path"] = url.absoluteString
            js["webPath"] = bridge?.portablePath(fromLocalURL: url)?.absoluteString
            js["name"] = url.lastPathComponent
            js["extension"] = url.pathExtension
            fileArray.append(js)
        }
        savedCall?.resolve(["files": fileArray])
    }

    public func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
        savedCall?.reject("canceled")
    }
}

extension FilePickerPlugin: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    public func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        guard let mediaUrl = info[.mediaURL] as? URL, let s = settings else {
            savedCall?.reject("canceled"); picker.dismiss(animated: true); return
        }
        let tmp = URL(fileURLWithPath: NSTemporaryDirectory()).appendingPathComponent(mediaUrl.lastPathComponent)
        do {
            try FileManager.default.copyItem(at: mediaUrl, to: tmp)
        } catch {
            savedCall?.reject("Cannot save video"); picker.dismiss(animated: true); return
        }

        let js: JSObject = [
            "path": tmp.absoluteString,
            "webPath": bridge?.portablePath(fromLocalURL: tmp)?.absoluteString ?? "",
            "name": tmp.lastPathComponent,
            "extension": tmp.pathExtension
        ]
        picker.dismiss(animated: true) {
            self.savedCall?.resolve(["files": [js]])
        }
    }

    public func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true) {
            self.savedCall?.reject("canceled")
        }
    }
}
