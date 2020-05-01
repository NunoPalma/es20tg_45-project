import User from '@/models/user/User';
import Topic from '@/models/management/Topic';
import {Quiz} from '@/models/management/Quiz';

export default class Tournament {
    tournamentId!: number;
    creator!: User;
    courseExecutionId!: number;
    name!: string;
    startDate!: string;
    endDate!: string;
    topics: Topic[] = [];
    numQuestions!: number;
    quiz!: Quiz;
    state!: string;
    isLoading: boolean = false;


    constructor(jsonObj?: Tournament) {
        if (jsonObj) {
            this.tournamentId = jsonObj.tournamentId;
            this.creator = jsonObj.creator; // you need to create the User object, no simply attribute from the json
            this.courseExecutionId = jsonObj.courseExecutionId;
            this.name = jsonObj.name;
            this.startDate = jsonObj.startDate;
            this.endDate = jsonObj.endDate;
            if (jsonObj.topics) {
                this.topics = jsonObj.topics.map(
                    (topic: Topic) => new Topic(topic)
                );
            }
            this.numQuestions = jsonObj.numQuestions;
            this.quiz = jsonObj.quiz; // you need to create the Quiz object, no simply attribute from the json
            this.state = jsonObj.state;
        }
    }
}
