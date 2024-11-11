<template>
    teste
</template>
<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const topics = ref([]);
const userPreferenceSendWhenReady = ref(false);

const fetchUserData = async() => {
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
        userPreferenceSendWhenReady.value = getData[1].isSendNotificationWhenReady
    } catch (error) {
        console.error(error.message);
    }
}


onMounted(() => {
    fetchUserData()
});

</script>