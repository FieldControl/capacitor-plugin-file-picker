import { WebPlugin } from '@capacitor/core';
import type { FilePickerCommonOptions, FilePickerOptions, FilePickerPlugin, FilePickerResults } from './definitions';
export declare class FilePickerWeb extends WebPlugin implements FilePickerPlugin {
    pick(options: FilePickerOptions): Promise<FilePickerResults>;
    pickImages(options?: FilePickerCommonOptions): Promise<FilePickerResults>;
    pickVideos(options?: FilePickerCommonOptions): Promise<FilePickerResults>;
    pickFiles(options?: FilePickerCommonOptions): Promise<FilePickerResults>;
}
