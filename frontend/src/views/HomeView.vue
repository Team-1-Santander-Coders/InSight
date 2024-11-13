<template>
    <div class="w-[98%] mx-auto my-4">
        <LandingPage />
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import LandingPage from '../components/LandingPage.vue'

const darkMode = ref(document.documentElement.classList.contains('dark'));

const updateDarkMode = () => {
    darkMode.value = document.documentElement.classList.contains('dark');
};

let observer: MutationObserver;

onMounted(() => {
    updateDarkMode();

    observer = new MutationObserver(() => {
        updateDarkMode();
    });

    observer.observe(document.documentElement, {
        attributes: true,
        attributeFilter: ['class']
    });
});

onUnmounted(() => {
    if (observer) {
        observer.disconnect();
    }
});
</script>

<style scoped></style>
