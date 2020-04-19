import Clarification from '@/models/management/Clarification';

export default class Doubt {
  id: number | null = null;
  author: string = '';
  status: string = 'UNSOLVED';
  content: string = '';
  clarificationDto: Clarification | null = null;

  constructor(jsonObj?: Doubt) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.author = jsonObj.author;
      this.status = jsonObj.status;
      this.content = jsonObj.content;
      this.clarificationDto = jsonObj.clarificationDto;
    }
  }
}
