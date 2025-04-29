export interface FilePickerPlugin {
    pick(options: FilePickerOptions): Promise<FilePickerResult[]>;
    pickImages(options?: FilePickerCommonOptions): Promise<FilePickerResult[]>;
    pickVideos(options?: FilePickerCommonOptions): Promise<FilePickerResult[]>;
    pickFiles(options?: FilePickerCommonOptions): Promise<FilePickerResult[]>;
}
export interface FilePickerCommonOptions {
    /**
     * Select multiple Files
     * @default false
     */
    multiple?: boolean;
    /**
     * Maximum number of files to select
     */
    limit?: number;
}
export interface FilePickerOptions extends FilePickerCommonOptions {
    mimes: string[];
}
export interface FilePickerResult {
    /**
     * File Path
     */
    path: string;
    /**
     * webPath returns a path that can be used to set the src attribute of an image for efficient
     * loading and rendering.
     */
    webPath: string;
    /**
     * File Name
     */
    name: string;
    /**
     * File Extensions
     */
    extension: string;
}
