import Clarification from '@/models/management/Clarification';

export default class Doubt {
  id: number | null = null;
  author: string = '';
  status: string = 'UNSOLVED';
  visibility: String = 'PRIVATE';
  content: string = '';
  clarificationDto: Clarification | null = null;
  questionTitle: string = '';
  title: string = '';
  creationDate!: String | null;
  isNew: Boolean = false;
  doubtType: string = 'PRINCIPAL';
  mainDoubtId: number | null = null;

  constructor(jsonObj?: Doubt) {
    if (jsonObj) {
      this.creationDate = jsonObj.creationDate;
      this.title = jsonObj.title;
      this.isNew = jsonObj.isNew;
      this.id = jsonObj.id;
      this.author = jsonObj.author;
      this.status = jsonObj.status;
      this.visibility = jsonObj.visibility;
      this.content = jsonObj.content;
      this.clarificationDto = jsonObj.clarificationDto;
      this.questionTitle = jsonObj.questionTitle;
      this.doubtType = jsonObj.doubtType;
      this.mainDoubtId = jsonObj.mainDoubtId;
    }
  }
}
