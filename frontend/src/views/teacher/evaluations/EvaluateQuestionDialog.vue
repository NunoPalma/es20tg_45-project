o<template>
  <v-dialog :value="dialog"
            @input="$emit('dialog', false)"
            @keydown.esc="$emit('dialog', false)"
            max-width="75%">
    <v-card>
      <v-card-title>
        <span class="headline">{{question.title}}</span>
      </v-card-title>

      <v-card-text class="text-left" v-if="question">
        <div>
          <span v-html="convertMarkDown(question.content, question.image)" />
          <ul>
            <li v-for="option in question.options" :key="option.number">
              <span
                      v-if="option.correct"
                      v-html="convertMarkDown('**[★]** ', null)"
              />
              <span
                      v-html="convertMarkDown(option.content, null)"
                      v-bind:class="[option.correct ? 'font-weight-bold' : '']"
              />
            </li>
          </ul>
          <br />
        </div>
      </v-card-text>

      <v-card-text class="text-left">
        <v-switch
                v-model="statusDefault"
                label="Question Approved"
                data-cy="approve"
        />
      </v-card-text>

      <v-card-text class="text-left">
        <div>
          <v-text-field v-model="justification" label="Justification" data-cy="justification"></v-text-field>
        </div>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn color="blue darken-1" @click="closeDialogue">Cancel</v-btn>
        <v-btn color="blue darken-1" @click="evaluateQuestion" data-cy="saveEvaluation">Save</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
  import { Component, Prop, Vue, Model } from 'vue-property-decorator';
  import Question from '../../../models/management/Question';
  import Evaluation from '@/models/management/Evaluation';
  import RemoteServices from '@/services/RemoteServices';
  import Image from '@/models/management/Image';
  import { convertMarkDown } from '@/services/ConvertMarkdownService';

  @Component
  export default class EvaluateQuestionDialog extends Vue {
    @Prop({type: Question, required: true}) readonly question!: Question;
    @Model('dialog', Boolean) dialog!: boolean;

    //trying to merge

    evaluation!: Evaluation;
    editQuestion!: Question;
    statusDefault = false;
    justification = '';

    async created() {
      this.evaluation = new Evaluation(await RemoteServices.findEvaluation(this.question));
      this.editQuestion = this.question;
    }

    // https://github.com/F-loat/vue-simplemde/blob/master/doc/configuration_en.md
    markdownConfigs: object = {
      status: false,
      spellChecker: false,
      insertTexts: {
        image: ['![image][image]', '']
      }
    };

    async setStatus(status: boolean) {
      try {
        if(status){
          if(this.question.id){
            let questionNew = await RemoteServices.setQuestionStatus(this.question.id, 'AVAILABLE');
            if (questionNew) {
              this.editQuestion.status = 'AVAILABLE';
            }
          }
        }
        else{
          if(this.question.id){
            let questionNew = await RemoteServices.setQuestionStatus(this.question.id, 'REJECTED');
            if (questionNew) {
              this.editQuestion.status = 'REJECTED';
            }
          }
        }

      } catch (error) {
        await this.$store.dispatch('error', error);
      }
    }

    convertMarkDown(text: string, image: Image | null = null): string {
      return convertMarkDown(text, image);
    }

    closeDialogue() {
      this.$emit('close-evaluate-question-dialog');
    }

    async evaluateQuestion() {
      if(this.evaluation && (this.statusDefault === false && (this.justification === '' || this.justification === null))){
        await this.$store.dispatch('error', 'Rejected Question must have a justification!');
        return;
      }
      if(this.evaluation && this.question){
        try{
          if(this.statusDefault === true){
            this.evaluation.approvedEvaluation = true;
          }
          else if(this.statusDefault === false){
            this.evaluation.approvedEvaluation = false;
          }
          await this.setStatus(this.statusDefault);
          this.evaluation.justification = this.justification;
          const result = await RemoteServices.submitEvaluation(this.evaluation, this.question);
          this.$emit('evaluate-question', result);
        } catch (error) {
          await this.$store.dispatch('error', error);
        }
      }
    }
  }
</script>

<style scoped></style>