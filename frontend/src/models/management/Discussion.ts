import Doubt from '@/models/management/Doubt';

export default class Discussion {
  postsDto: Doubt[] = [];
  id: number | null = null;
  visibility: String = 'PRIVATE';
  questionTitle: string = '';
  title: string = '';
  author: string = '';

  constructor(jsonObj?: Discussion) {
    if (jsonObj) {
      this.title = jsonObj.title;
      this.id = jsonObj.id;
      this.visibility = jsonObj.visibility;
      this.questionTitle = jsonObj.questionTitle;
      this.postsDto = jsonObj.postsDto.map((doubt: Doubt) => new Doubt(doubt));
      this.author = jsonObj.author;
    }
  }
}
