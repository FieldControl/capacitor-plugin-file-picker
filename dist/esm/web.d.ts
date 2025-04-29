import { WebPlugin } from '@capacitor/core';
import type { FilePickerCommonOptions, FilePickerOptions, FilePickerPlugin, FilePickerResult } from './definitions';
export declare class FilePickerWeb extends WebPlugin implements FilePickerPlugin {
    pick(options: FilePickerOptions): Promise<FilePickerResult[]>;
    pickImages(options?: FilePickerCommonOptions): Promise<FilePickerResult[]>;
    pickVideos(options?: FilePickerCommonOptions): Promise<FilePickerResult[]>;
    pickFiles(options?: FilePickerCommonOptions): Promise<FilePickerResult[]>;
}
