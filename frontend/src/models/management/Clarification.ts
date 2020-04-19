import Option from '@/models/management/Option';
import Image from '@/models/management/Image';
import Topic from '@/models/management/Topic';

export default class Clarification {
  author: string = '';
  description: string = '';

  constructor(jsonObj?: Clarification) {
    if (jsonObj) {
      this.author = jsonObj.author;
      this.description = jsonObj.description;

    }
  }
}
