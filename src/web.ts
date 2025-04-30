import { WebPlugin } from '@capacitor/core';

import type {
  FilePickerCommonOptions,
  FilePickerOptions,
  FilePickerPlugin,
  FilePickerResults,
} from './definitions';

export class FilePickerWeb extends WebPlugin implements FilePickerPlugin {
  async pick(options: FilePickerOptions): Promise<FilePickerResults> {
    console.log('pick', options);

    throw this.unimplemented('Not implemented on web.');
  }

  async pickImages(
    options?: FilePickerCommonOptions,
  ): Promise<FilePickerResults> {
    console.log('pickImages', options);

    throw this.unimplemented('Not implemented on web.');
  }

  async pickVideos(
    options?: FilePickerCommonOptions,
  ): Promise<FilePickerResults> {
    console.log('pickVideos', options);

    throw this.unimplemented('Not implemented on web.');
  }

  async pickFiles(
    options?: FilePickerCommonOptions,
  ): Promise<FilePickerResults> {
    console.log('pickFiles', options);

    throw this.unimplemented('Not implemented on web.');
  }
}
