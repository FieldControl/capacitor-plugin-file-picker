var capacitorFilePicker = (function (exports, core) {
    'use strict';

    const FilePicker = core.registerPlugin('FilePicker', {
        web: () => Promise.resolve().then(function () { return web; }).then(m => new m.FilePickerWeb()),
    });

    class FilePickerWeb extends core.WebPlugin {
        async pick(options) {
            console.log('pick', options);
            throw this.unimplemented('Not implemented on web.');
        }
        async pickImages(options) {
            console.log('pickImages', options);
            throw this.unimplemented('Not implemented on web.');
        }
        async pickVideos(options) {
            console.log('pickVideos', options);
            throw this.unimplemented('Not implemented on web.');
        }
        async pickFiles(options) {
            console.log('pickFiles', options);
            throw this.unimplemented('Not implemented on web.');
        }
    }

    var web = /*#__PURE__*/Object.freeze({
        __proto__: null,
        FilePickerWeb: FilePickerWeb
    });

    exports.FilePicker = FilePicker;

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
