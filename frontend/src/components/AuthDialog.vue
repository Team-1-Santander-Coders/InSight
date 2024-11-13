<template>
    <Dialog v-model:visible="userModalVisible" modal :header="dialogHeader" :style="{ width: '25rem' }">
        <span class="text-surface-500 dark:text-surface-400 block mb-8">Digite seus dados</span>
        <div class="flex justify-evenly">
            <div v-for="category in categories" :key="category.key" class="flex flex-col justify-center gap-4 mb-8">
                <div>
                    <RadioButton v-model="selectedCategory" :inputId="category.key" name="authType"
                        :value="category.key" :label="category.name" />
                    <label :for="category.key" class="ml-2">{{ category.name }}</label>
                </div>
            </div>
        </div>

        <div v-if="selectedCategory === 'login'">
            <form v-on:submit="submitForm">
                <div class="flex items-center gap-4 mb-4">
                    <label for="login" class="font-semibold w-24">Usuário/Email</label>
                    <InputText id="login" v-model="login" class="flex-auto" placeholder="Digite seu usuário/email"
                        required autocomplete="off" />
                </div>
                <div class="flex items-center gap-4 mb-8">
                    <label for="password" class="font-semibold w-24">Senha</label>
                    <InputText id="password" type="password" v-model="password" feedback="false" class="flex-auto"
                        placeholder="Digite sua senha" required autocomplete="off" />
                </div>
                <div class="flex justify-end gap-2">
                    <Button type="submit" :label="buttonLabel" :loading="loading" class="mx-auto"></Button>
                </div>
            </form>
        </div>

        <div v-if="selectedCategory === 'register'">
            <form v-on:submit="submitForm">
                <div class="flex items-center gap-4 mb-4">
                    <label for="name" class="font-semibold w-24">Nome completo</label>
                    <InputText id="name" v-model="name" class="flex-auto" type="text" placeholder="Digite seu nome"
                        required autocomplete="off" />
                </div>
                <div class="flex items-center gap-4 mb-2">
                    <label for="username" class="font-semibold w-24">Usuário</label>
                    <InputText id="username" v-model="username" class="flex-auto" type="text"
                        placeholder="Digite seu usuário" required autocomplete="off" />
                </div>
                <div class="flex items-center gap-4 mb-4">
                    <label for="email" class="font-semibold w-24">Email</label>
                    <InputText id="email" v-model="email" class="flex-auto" type="email" placeholder="Digite seu email"
                        required autocomplete="off" />
                </div>
                <div class="flex items-center gap-4 mb-2">
                    <label for="password" class="font-semibold w-24">Senha</label>
                    <Password id="password" v-model="password" feedback="false" class="flex-auto"
                        placeholder="Digite sua senha" required autocomplete="off" />
                </div>
                <div class="flex items-center gap-4 mb-4">
                    <label for="userType" class="font-semibold w-24">Tipo do documento</label>
                    <div class="flex-auto">
                        <div class="flex gap-2">
                            <Dropdown id="userType" v-model="documentType" :options="documentTypes" optionLabel="name"
                                optionValue="value" class="w-full" />
                        </div>
                    </div>
                </div>
                <div class="flex items-center gap-4 mb-4">
                    <label for="document" class="font-semibold w-24">Documento</label>
                    <div class="flex-auto">
                        <div class="flex gap-2">
                            <InputMask id="document" v-model="document"
                                :mask="documentType === 'cpf' ? '999.999.999-99' : '99.999.999/9999-99'"
                                :placeholder="documentType === 'cpf' ? 'Digite seu CPF' : 'Digite seu CNPJ'"
                                class="flex-auto" required autocomplete="off" />
                        </div>
                    </div>
                </div>
                <div class="flex items-center gap-4 mb-4">
                    <label for="birthDate" class="font-semibold w-24">Data de nascimento</label>
                    <div class="flex-auto">
                        <div class="flex gap-2">
                            <InputMask id="birthDate" v-model="birthDate" mask="99/99/9999"
                                placeholder="Digite sua data de nascimento" class="flex-auto" required
                                autocomplete="off" />
                        </div>
                    </div>
                </div>
                <div class="flex items-center gap-4 mb-4">
                    <label for="phoneNumber" class="font-semibold w-24">Telefone celular</label>
                    <div class="flex-auto">
                        <div class="flex gap-2">
                            <InputMask id="phoneNumber" v-model="phoneNumber" mask="(99) 99999-9999"
                                placeholder="Digite seu número de celular" class="flex-auto" required
                                autocomplete="off" />
                        </div>
                    </div>
                </div>
                <div class="flex justify-end gap-2">
                    <Button type="submit" :label="buttonLabel" :loading="loading" class="mx-auto"></Button>
                </div>
            </form>
        </div>
        <p class="text-center text-red-500 mb-3" v-if="errorMessage">{{ errorMessage }}</p>
    </Dialog>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import InputMask from 'primevue/inputmask';
import Password from 'primevue/password';
import Button from 'primevue/button';
import RadioButton from 'primevue/radiobutton';
import Dropdown from 'primevue/dropdown';
import axios from '../plugins/axios';

const userModalVisible = ref(false);
const selectedCategory = ref('login');
const name = ref('');
const username = ref('');
const birthDate = ref('');
const login = ref('');
const email = ref('');
const password = ref('');
const document = ref('');
const phoneNumber = ref('');
const documentType = ref('cpf');
const loading = ref(false);
const errorMessage = ref('');

const categories = ref([
    { name: 'Entrar', key: 'login' },
    { name: 'Registrar', key: 'register' },
]);

const documentTypes = [
    { name: 'CPF', value: 'cpf' },
    { name: 'CNPJ', value: 'cnpj' }
];

const dialogHeader = computed(() => {
    return selectedCategory.value === 'login' ? 'Entrar' : 'Registrar';
});

const buttonLabel = computed(() => {
    return selectedCategory.value === 'login' ? 'Entrar' : 'Registrar';
});

const submitForm = async () => {
    loading.value = true;
    errorMessage.value = '';
    try {
        if (selectedCategory.value === 'login') {
            const response = await axios.post('/login', {
                login: login.value,
                password: password.value
            });
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', login.value);
            console.log(response.status)
        } else if (selectedCategory.value === 'register') {
            const response = await axios.post('/register', {
                name: name.value,
                username: username.value,
                document: document.value,
                birthDate: birthDate.value,
                documentType: documentType.value,
                email: email.value,
                password: password.value,
                phone: phoneNumber.value,
            });
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', username.value);
        }
        userModalVisible.value = false;
        location.reload();
    } catch (error: any) {
        if (error.response) {
            errorMessage.value = error.response.data;
        } else {
            errorMessage.value = 'An unexpected error occurred';
        }
    } finally {
        loading.value = false;
        document.value = "";
        email.value = "";
        password.value = "";
        documentType.value = "cpf";
    }
};

defineExpose({
    userModalVisible
});
</script>