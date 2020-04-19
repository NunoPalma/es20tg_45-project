<template>
  <div class="quiz-container">
    <div class="question-navigation">
      <div class="navigation-buttons">
        <span
          v-for="index in +statementManager.statementQuiz.questions.length"
          v-bind:class="[
            'question-button',
            index === questionOrder + 1 ? 'current-question-button' : '',
            index === questionOrder + 1 &&
            statementManager.correctAnswers[index - 1].correctOptionId !==
              statementManager.statementQuiz.answers[index - 1].optionId
              ? 'incorrect-current'
              : '',
            statementManager.correctAnswers[index - 1].correctOptionId !==
            statementManager.statementQuiz.answers[index - 1].optionId
              ? 'incorrect'
              : ''
          ]"
          :key="index"
          @click="changeOrder(index - 1)"
        >
          {{ index }}
        </span>
      </div>
      <span
        class="left-button"
        @click="decreaseOrder"
        v-if="questionOrder !== 0"
        ><i class="fas fa-chevron-left"
      /></span>
      <span
        class="right-button"
        @click="increaseOrder"
        v-if="
          questionOrder !== statementManager.statementQuiz.questions.length - 1
        "
        ><i class="fas fa-chevron-right"
      /></span>
    </div>
    <result-component
      v-model="questionOrder"
      :answer="statementManager.statementQuiz.answers[questionOrder]"
      :correctAnswer="statementManager.correctAnswers[questionOrder]"
      :question="statementManager.statementQuiz.questions[questionOrder]"
      :questionNumber="statementManager.statementQuiz.questions.length"
      @increase-order="increaseOrder"
      @decrease-order="decreaseOrder"
    />
    <new-doubt-dialog
      v-if="doubt"
      v-model="createDoubtDialog"
      :doubt="doubt"
      :quizId="quizQuestionId"
      v-on:new-doubt="onCreateDoubt"
      v-on:close-dialog="onCloseDialog"
    />
    <div class="container">
      <h2>Current Discussions About This Question</h2>
      <ul>
        <li class="list-header ">
          <div class="col">Author</div>
          <div class="col">Content</div>
          <div class="col">Status</div>
          <div class="col last-col"></div>
        </li>
        <li
                class="list-row"
                v-for="doubt in doubts[this.questionOrder]"
                :key="doubt.id"
        >
          <div class="col">
            {{ doubt.author }}
          </div>
          <div class="col">
            {{ doubt.content }}
          </div>
          <div class="col">
            {{ doubt.status }}
          </div>
          <div class="col last-col">
            <i class="fas fa-chevron-circle-right" />
          </div>
        </li>
      </ul>
    </div>
    <v-btn width="1040px" large color="primary" dark @click="newDoubt">
      <v-icon left dark>mdi-plus</v-icon>New Doubt</v-btn>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import StatementManager from '@/models/statement/StatementManager';
import ResultComponent from '@/views/student/quiz/ResultComponent.vue';
import Doubt from '@/models/management/Doubt';
import CreateDoubtDialog from '@/views/student/CreateDoubtDialog.vue';
import RemoteServices from '@/services/RemoteServices';

@Component({
  components: {
    'result-component': ResultComponent,
    'new-doubt-dialog': CreateDoubtDialog
  }
})
export default class ResultsView extends Vue {
  statementManager: StatementManager = StatementManager.getInstance;
  quizQuestionId: number = 0;
  questionOrder: number = 0;
  createDoubtDialog: boolean = false;

  doubt: Doubt | null = null;
  doubts: Doubt[][] = [];

  async created() {
    if (this.statementManager.isEmpty()) {
      await this.$router.push({name: 'create-quiz'});
    }
    if(this.statementManager.statementQuiz != null){
      for(var iter = 0; iter < this.statementManager.statementQuiz?.questions.length; iter++ ){
        this.doubts[iter] = await RemoteServices.getQuestionDoubts(this.statementManager.correctAnswers[iter].quizQuestionId);
        console.log(this.doubts);
      }
    }
  }

  increaseOrder(): void {
    if (this.questionOrder + 1 < +this.statementManager.statementQuiz!.questions.length) {
      this.questionOrder += 1;
    }
  }

  decreaseOrder(): void {
    if (this.questionOrder > 0) {
      this.questionOrder -= 1;
    }
  }

  changeOrder(n: number): void {
    if (n >= 0 && n < +this.statementManager.statementQuiz!.questions.length) {
      this.questionOrder = n;
    }
  }

  newDoubt(): void {
    this.quizQuestionId = this.statementManager.correctAnswers[
      this.questionOrder
    ].quizQuestionId;
    console.log(this.statementManager.statementQuiz);
    this.doubt = new Doubt();
    this.createDoubtDialog = true;
  }

  async onCreateDoubt() {
    this.createDoubtDialog = false;
    this.doubt = null;
  }

  onCloseDialog() {
    this.createDoubtDialog = false;
    this.doubt = null;
  }
}
</script>

<style lang="scss" scoped>
  .container {
    max-width: 1040px;
    margin-left: auto;
    margin-right: auto;
    padding-left: 10px;
    padding-right: 10px;

    h2 {
      font-size: 26px;
      margin: 20px 0;
      text-align: center;
      small {
        font-size: 0.5em;
      }
    }

    ul {
      overflow: hidden;
      padding: 0 5px;

      li {
        border-radius: 3px;
        padding: 15px 10px;
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;
      }

      .list-header {
        background-color: #1976d2;
        color: white;
        font-size: 14px;
        text-transform: uppercase;
        letter-spacing: 0.03em;
        text-align: center;
      }

      .col {
        width: 25%;
      }

      .last-col {
        max-width: 50px !important;
      }

      .list-row {
        background-color: #ffffff;
        cursor: pointer;
        box-shadow: 0 0 9px 0 rgba(0, 0, 0, 0.1);
      }

      .list-row:hover {
        background-color: #c8c8c8;
      }
    }
  }
.incorrect {
  color: #cf2323 !important;
}

.incorrect-current {
  background-color: #cf2323 !important;
  color: #fff !important;

}
</style>
