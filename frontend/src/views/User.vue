<template>
    <div class="card w-[98%]">
        <Card class="w-full p-8">
            <template #header>
                <div class="w-full h-min flex flex-row">
                    <span class="card-about-title mr-auto">
                        Seus InSights
                    </span>
                    <form @action="submitTopic">
                        <div class="w-full flex flex-row items-center justify-center gap-3">
                            <div class="flex gap-2 items-center">
                                <label for="topic" class="font-semibold">Título do tópico:</label>
                                <InputText id="topic" v-model="topicTitle" class="flex-auto" type="text"
                                    placeholder="Digite seu nome" required autocomplete="off" />
                            </div>
                            <div class="flex justify-end">
                                <Button label="Adicionar tópico" severity="primary" type="submit" />
                            </div>
                        </div>
                    </form>
                </div>
            </template>
            <template #content>
                <DataTable v-model:expandedRowGroups="expandedRowGroups" :value="topics" rowGroupMode="subheader"
                    groupRowsBy="title" sortMode="single" sortField="title" :sortOrder="1"
                    tableStyle="min-width: 50rem">
                    <Column field="title" header="Título" style="display: none"></Column>
                    <Column field="image" header="Ícone" style="width: 200px">
                        <template #body="slotProps">
                            <div v-if="slotProps.data.summaries && slotProps.data.summaries.length"
                                class="flex flex-col gap-4">
                                <img v-for="summary in slotProps.data.summaries" :key="summary.id" :src="summary.image"
                                    :alt="slotProps.data.title" class="w-32 h-32 object-cover rounded-lg" />
                            </div>
                        </template>
                    </Column>
                    <Column field="description" header="Descrição" style="min-width: 300px">
                        <template #body="slotProps">
                            <div v-if="slotProps.data.summaries && slotProps.data.summaries.length"
                                class="flex flex-col gap-4">
                                <p v-for="summary in slotProps.data.summaries" :key="summary.id" class="text-sm">
                                    {{ summary.description }}
                                </p>
                            </div>
                        </template>
                    </Column>
                    <Column field="initialDate" header="Data inicial" style="width: 150px">
                        <template #body="slotProps">
                            <div v-if="slotProps.data.summaries && slotProps.data.summaries.length"
                                class="flex flex-col gap-4">
                                <span v-for="summary in slotProps.data.summaries" :key="summary.id">
                                    {{ formatDate(summary.initialDate) }}
                                </span>
                            </div>
                        </template>
                    </Column>
                    <Column field="finalDate" header="Data final" style="width: 150px">
                        <template #body="slotProps">
                            <div v-if="slotProps.data.summaries && slotProps.data.summaries.length"
                                class="flex flex-col gap-4">
                                <span v-for="summary in slotProps.data.summaries" :key="summary.id">
                                    {{ formatDate(summary.finalDate) }}
                                </span>
                            </div>
                        </template>
                    </Column>
                    <Column field="audio" header="Áudio" style="width: 150px">
                        <template #body="slotProps">
                            <div v-if="slotProps.data.summaries && slotProps.data.summaries.length"
                                class="flex flex-col gap-4">
                                <Button v-for="summary in slotProps.data.summaries" :key="summary.id"
                                    label="Ouvir InSight" severity="primary" size="small"
                                    @click="openAudio(summary.audio)" />
                                <Button v-for="summary in slotProps.data.summaries" :key="summary.id"
                                    label="Consultar InSight" severity="contrast" size="small"
                                    @click="openSummaryDialog(summary)" />
                            </div>
                        </template>
                    </Column>

                    <template #groupheader="{ data }">
                        <div class="flex justify-between items-center w-full pr-4">
                            <span class="font-bold text-lg">{{ data.title }}</span>
                            <Button label="Gerar Resumo" severity="secondary" size="small"
                                @click="openGenerateDialog(data)" />
                        </div>
                    </template>
                </DataTable>
            </template>
        </Card>

        <Dialog v-model:visible="dialogVisible" header="Gerar Resumo" :modal="true" :style="{ width: '400px' }">
            <div class="flex flex-col gap-4">
                <div v-for="option in periodOptions" :key="option.value" class="flex items-center gap-2">
                    <RadioButton v-model="selectedPeriod" :value="option.value" :name="option.value" />
                    <label :for="option.value">{{ option.label }}</label>
                </div>
            </div>

            <template #footer>
                <div class="flex justify-end gap-2">
                    <Button label="Cancelar" severity="secondary" @click="closeDialog" size="small" />
                    <Button label="Gerar" severity="primary" @click="generateSummary" size="small" />
                </div>
            </template>
        </Dialog>

        <Dialog v-model:visible="summaryDialogVisible" header="InSight" :modal="true" :style="{ width: '400px' }">

            <span>
                {{ generatedSummary }}
            </span>
            <template #footer>
                <div class="flex justify-end gap-2">
                    <Button label="Fechar" severity="secondary" @click="closeSummaryDialog" size="small" />
                </div>
            </template>
        </Dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import RadioButton from 'primevue/radiobutton';
import InputText from 'primevue/inputtext';
import { useToast } from 'primevue/usetoast';
import Card from 'primevue/card';

const expandedRowGroups = ref();
const topics = ref([]);
const userPreferenceSendWhenReady = ref(false);

const toast = useToast();
const dialogVisible = ref(false);
const topicTitle = ref("");
const summaryDialogVisible = ref(false);
const generatedSummary = ref("");
const selectedPeriod = ref(null);
const selectedTopic = ref(null);
const selectedSummary = ref(null);

const periodOptions = [
    { label: 'Dia', value: 'day' },
    { label: 'Semana', value: 'week' },
    { label: 'Mês', value: 'month' }
];

const submitTopic = async () => {
    addTopic(topicTitle)
}

const openGenerateDialog = (topic) => {
    selectedTopic.value = topic;
    selectedPeriod.value = null;
    dialogVisible.value = true;
};

const openSummaryDialog = async (summary) => {
    await fetchSummaryData(summary.id);
    selectedSummary.value = summary;
    summaryDialogVisible.value = true;
};

const closeSummaryDialog = () => {
    summaryDialogVisible.value = false;
    selectedSummary.value = null;
    generatedSummary.value = "";
};

const closeDialog = () => {
    dialogVisible.value = false;
    selectedPeriod.value = null;
    selectedTopic.value = null;
};

const generateSummary = async () => {
    if (!selectedPeriod.value || !selectedTopic.value) return;
    const payload = {
        topic_id: selectedTopic.value.id,
        period: selectedPeriod.value
    }
    
    try {
        const getResponse = await fetch('http://localhost:8080/summarize', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(payload)
        });
        closeDialog();
        if (getResponse.ok) {
            toast.add({ severity: 'success', summary: 'Sucesso!', detail: `Seu InSight estará pronto em breve!`, life: 3000 });
        }
        else {
            toast.add({ severity: 'info', summary: 'Informação', detail: `${getResponse.text()}`, life: 3000 });
        }
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Erro', detail: `Erro ao buscar o valor: ${error.message}`, life: 3000 });
    }
};

const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('pt-BR');
};

const openAudio = (audioUrl) => {
    window.open(audioUrl, '_blank');
};

const fetchUserData = async () => {
    try {
        const getResponse = await fetch('http://localhost:8080/user', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (!getResponse.ok) {
            throw new Error(`Erro ${getResponse.status}: ${getResponse.statusText}`);
        }

        const responseText = await getResponse.text();

        if (!responseText) {
            throw new Error('Resposta vazia do servidor.');
        }
        const getData = JSON.parse(responseText);
        topics.value = getData[0]
        userPreferenceSendWhenReady.value = getData[1].isSendNotificationWhenReady;
    } catch (error) {
        console.error(error.message);
    }
}


const fetchSummaryData = async (id) => {
    try {
        const getResponse = await fetch(`http://localhost:8080/summary/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (!getResponse.ok) {
            throw new Error(`Erro ${getResponse.status}: ${getResponse.statusText}`);
        }

        const responseText = await getResponse.text();

        if (!responseText) {
            throw new Error('Resposta vazia do servidor.');
        }
        const getData = JSON.parse(responseText);
        generatedSummary.value = getData.summary;
    } catch (error) {
        console.error(error.message);
    }
}

const addTopic = async (topicTitle) => {
    const payload = {
        title: topicTitle,
    }

    try {
        const getResponse = await fetch('http://localhost:8080/topic', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(payload)
        });
        closeDialog();
        if (getResponse.ok) {
            toast.add({ severity: 'success', summary: 'Sucesso!', detail: `Seu InSight estará pronto em breve!`, life: 3000 });
        }
        else {
            toast.add({ severity: 'info', summary: 'Informação', detail: `${getResponse.text()}`, life: 3000 });
        }
        location.reload();
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Erro', detail: `Erro ao buscar o valor: ${error.message}`, life: 3000 });
    } 
};

onMounted(() => {
    fetchUserData();
    expandedRowGroups.value = topics.value.map(topic => topic.title);
});
</script>
