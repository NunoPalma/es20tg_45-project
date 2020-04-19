o<template>
    <v-card class="table">
        <v-data-table
                :headers="headers"
                :custom-filter="customFilter"
                :items="questions"
                :search="search"
                multi-sort
                :mobile-breakpoint="0"
                :items-per-page="15"
                :footer-props="{ itemsPerPageOptions: [15, 30, 50, 100] }"
        >
            <template v-slot:top>
                <v-card-title>
                    <v-text-field
                            v-model="search"
                            append-icon="search"
                            label="Search"
                            class="mx-2"
                    />

                    <v-spacer />
                    <v-btn color="primary" dark @click="newQuestion">New Question</v-btn>
                </v-card-title>
            </template>

            <template v-slot:item.content="{ item }">
                <p
                        v-html="convertMarkDownNoFigure(item.content, null)"
                        @click="showQuestionDialog(item)"
                /></template>

            <template v-slot:item.topics="{ item }">
                <edit-question-topics
                        :question="item"
                        :topics="topics"
                        v-on:question-changed-topics="onQuestionChangedTopics"
                />
            </template>


            <template v-slot:item.status="{ item }">
                <v-chip :color="getStatusColor(item.status)" small>
                    <span>{{ item.status }}</span>
                </v-chip>
            </template>


            <template v-slot:item.creationDate="{ item }">
                {{ item.creationDate }}
            </template>

            <template v-slot:item.image="{ item }">
                <v-file-input
                        show-size
                        dense
                        small-chips
                        @change="handleFileUpload($event, item)"
                        accept="image/*"
                />
            </template>

            <template v-slot:item.action="{ item }">
                <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                        <v-icon
                                small
                                class="mr-2"
                                v-on="on"
                                @click="showQuestionDialog(item)"
                        >visibility</v-icon
                        >
                    </template>
                    <span>Show Question</span>
                </v-tooltip>
                <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                        <v-icon small class="mr-2" v-on="on" @click="editQuestion(item)"
                        >edit</v-icon
                        >
                    </template>
                    <span>Edit Question</span>
                </v-tooltip>
                <v-tooltip bottom>
                    <template v-slot:activator="{ on }">
                        <v-icon
                                small
                                class="mr-2"
                                v-on="on"
                                @click="deleteQuestion(item)"
                                color="red"
                        >delete</v-icon
                        >
                    </template>
                    <span>Delete Question</span>
                </v-tooltip>
            </template>
        </v-data-table>
        <edit-question-dialog
                v-if="currentQuestion"
                :dialog="editQuestionDialog"
                :question="currentQuestion"
                v-on:close-edit-question-dialog="onCloseEditQuestionDialogue"
                v-on:save-question="onSaveQuestion"
        />
        <show-question-dialog
                v-if="currentQuestion"
                :dialog="questionDialog"
                :question="currentQuestion"
                v-on:close-show-question-dialog="onCloseShowQuestionDialog"
        />


    </v-card>
</template>

<script lang="ts">
  import { Component, Vue } from 'vue-property-decorator';
  import RemoteServices from '@/services/RemoteServices';
  import { convertMarkDownNoFigure } from '@/services/ConvertMarkdownService';
  import Question from '@/models/management/Question';
  import Image from '@/models/management/Image';
  import Topic from '@/models/management/Topic';
  import EditSubmittedQuestionDialog from '@/views/student/question/EditSubmittedQuestionDialog.vue';
  import ShowSubmittedQuestionDialog from '@/views/student/question/ShowSubmittedQuestionDialog.vue';
  import EditSubmittedQuestionTopics from '@/views/student/question/EditSubmittedQuestionTopics.vue';

  @Component({
    components: {
      'show-question-dialog': ShowSubmittedQuestionDialog,
      'edit-question-dialog': EditSubmittedQuestionDialog,
      'edit-question-topics': EditSubmittedQuestionTopics
    }
  })
  export default class SubmittedQuestionsView extends Vue {
    questions: Question[] = [];
    topics: Topic[] = [];
    currentQuestion: Question | null = null;
    editQuestionDialog: boolean = false;
    questionDialog: boolean = false;
    search: string = '';

    headers: object = [
      { text: 'Question', value: 'content', align: 'left' },
      {
        text: 'Topics',
        value: 'topics',
        align: 'center',
        sortable: false
      },
      { text: 'Title', value: 'title', align: 'center' },
      { text: 'Status', value: 'status', align: 'center' },
      {
        text: 'Creation Date',
        value: 'creationDate',
        align: 'center'
      },
      {
        text: 'Image',
        value: 'image',
        align: 'center',
        sortable: false
      },
      {
        text: 'Actions',
        value: 'action',
        align: 'center',
        sortable: false
      }
    ];

    async created() {
      await this.$store.dispatch('loading');
      try {
        [this.topics, this.questions] = await Promise.all([
          RemoteServices.getTopics(),
          RemoteServices.getSubmittedQuestions()
        ]);
      } catch (error) {
        await this.$store.dispatch('error', error);
      }
      await this.$store.dispatch('clearLoading');
    }

    customFilter(value: string, search: string, question: Question) {
      // noinspection SuspiciousTypeOfGuard,SuspiciousTypeOfGuard
      return (
        search != null &&
        JSON.stringify(question)
          .toLowerCase()
          .indexOf(search.toLowerCase()) !== -1
      );
    }

    convertMarkDownNoFigure(text: string, image: Image | null = null): string {
      return convertMarkDownNoFigure(text, image);
    }

    onQuestionChangedTopics(questionId: Number, changedTopics: Topic[]) {
      let question = this.questions.find(
        (question: Question) => question.id == questionId
      );
      if (question) {
        question.topics = changedTopics;
      }
    }

    getStatusColor(status: string) {
      if (status === 'REMOVED') return 'red';
      else if (status === 'DISABLED') return 'orange';
      else if (status === 'REJECTED') return 'blue';
      else if (status === 'PENDING') return 'yellow';
      else return 'green';
    }

    async handleFileUpload(event: File, question: Question) {
      if (question.id) {
        try {
          const imageURL = await RemoteServices.uploadImage(event, question.id);
          question.image = new Image();
          question.image.url = imageURL;
          confirm('Image ' + imageURL + ' was uploaded!');
        } catch (error) {
          await this.$store.dispatch('error', error);
        }
      }
    }

    showQuestionDialog(question: Question) {
      this.currentQuestion = question;
      this.questionDialog = true;
    }

    onCloseShowQuestionDialog() {
      this.questionDialog = false;
    }

    newQuestion() {
      this.currentQuestion = new Question();
      this.editQuestionDialog = true;
    }

    editQuestion(question: Question) {
      if(question.status === 'REJECTED'){
        this.currentQuestion = question;
        this.editQuestionDialog = true;
      }else {
        alert('You can only edit a Rejected Question for resubmission');
      }
    }

    async onSaveQuestion(question: Question) {
      this.questions = this.questions.filter(q => q.id !== question.id);
      this.questions.unshift(question);
      this.onCloseEditQuestionDialogue();
    }

    onCloseEditQuestionDialogue() {
      this.editQuestionDialog = false;
      this.currentQuestion = null;
    }

    async deleteQuestion(toDeletequestion: Question) {
      if (
        toDeletequestion.id &&
        confirm('Are you sure you want to delete this question?')
      ) {
        try {
          await RemoteServices.deleteQuestion(toDeletequestion.id);
          this.questions = this.questions.filter(
            question => question.id != toDeletequestion.id
          );
        } catch (error) {
          await this.$store.dispatch('error', error);
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
    .question-textarea {
        text-align: left;

        .CodeMirror,
        .CodeMirror-scroll {
            min-height: 200px !important;
        }
    }
    .option-textarea {
        text-align: left;

        .CodeMirror,
        .CodeMirror-scroll {
            min-height: 100px !important;
        }
    }
</style>