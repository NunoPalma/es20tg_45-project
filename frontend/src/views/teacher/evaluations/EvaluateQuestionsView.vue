<template>
    <!--    <v-card class="table">-->
    <!--        <v-data-table-->
    <!--                :headers="headers"-->
    <!--                :custom-filter="customFilter"-->
    <!--                :items="questions"-->
    <!--                :search="search"-->
    <!--                multi-sort-->
    <!--                :mobile-breakpoint="0"-->
    <!--                :items-per-page="15"-->
    <!--                :footer-props="{ itemsPerPageOptions: [15, 30, 50, 100] }"-->
    <!--        >-->
    <!--            <template v-slot:top>-->
    <!--                <v-card-title>-->
    <!--                    <v-text-field-->
    <!--                            v-model="search"-->
    <!--                            append-icon="search"-->
    <!--                            label="Search"-->
    <!--                            class="mx-2"-->
    <!--                    />-->
    <!--                </v-card-title>-->
    <!--            </template>-->

    <!--            <template v-slot:item.content="{ item }">-->
    <!--                <p-->
    <!--                        v-html="convertMarkDownNoFigure(item.content, null)"-->
    <!--                        @click="showQuestionDialog(item)"-->
    <!--                /></template>-->

    <!--            <template v-slot:item.topics="{ item }">-->
    <!--                <edit-question-topics-->
    <!--                        :question="item"-->
    <!--                        :topics="topics"-->
    <!--                        v-on:question-changed-topics="onQuestionChangedTopics"-->
    <!--                />-->
    <!--            </template>-->

    <!--            <template v-slot:item.status="{ item }">-->
    <!--                <v-chip :color="getStatusColor(item.status)" small>-->
    <!--                    <span>{{ item.status }}</span>-->
    <!--                </v-chip>-->
    <!--            </template>-->

    <!--            <template v-slot:item.creationDate="{ item }">-->
    <!--                {{ item.creationDate }}-->
    <!--            </template>-->

    <!--            <template v-slot:item.image="{ item }">-->
    <!--                <v-file-input-->
    <!--                        show-size-->
    <!--                        dense-->
    <!--                        small-chips-->
    <!--                        @change="handleFileUpload($event, item)"-->
    <!--                        accept="image/*"-->
    <!--                />-->
    <!--            </template>-->
    <!--            &lt;!&ndash; NÂO ESQUECER DO RESTO DA IMPLEMENTAÇÃO &ndash;&gt;-->
</template>

<script>
    import { Component, Vue } from 'vue-property-decorator';
    import RemoteServices from '@/services/RemoteServices';
    import { convertMarkDownNoFigure } from '@/services/ConvertMarkdownService';
    import Question from '@/models/management/Question';

    @Component
    export default class EvaluateQuestionsView extends Vue {
        //questions: Question[] = [];
        // currentQuestion: Question | null = null;
        // evaluateQuestionDialog: boolean = false;
        // evaluationDialog: boolean = false;
        // search: string = '';
    };
</script>

<style scoped>

</style>