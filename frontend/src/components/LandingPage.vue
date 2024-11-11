<template>
    <div class="flex flex-col items-center justify-center w-full h-min">
        <div class="flex flex-col w-full items-center justify-center">
            <div class="landing-card w-full flex flex-row items-center justify-center">
                <div class="w-[48%] flex items-center justify-center flex-col">
                    <span class="card-title mb-4">
                        InSight
                    </span>
                    <span class="card-text">
                        Descubra novas perspectivas para transformar seus pensamentos.
                    </span>
                </div>
                <div class="landing-card-text w-[48%] flex items-center justify-center">
                    <div class="w-full flex flex-row items-center justify-evenly mb-12 mt-12">
                        <div class="w-[40%] text-center items-center justify-center">
                            <span class="card-text">
                                No <strong>InSight</strong>, acreditamos que o excesso de informação pode atrapalhar
                                quem quer se manter
                                bem-informado. Nossa plataforma foi feita para <strong>simplificar essa
                                    experiência</strong>.<br><br>

                                Com o InSight, você escolhe os <strong>temas importantes</strong> para você, e o sistema
                                coleta, organiza
                                e filtra as informações mais relevantes de redes sociais, sites de notícias e pesquisas
                                em tempo real.<br><br>

                                Assim, você acessa apenas o conteúdo que <strong>realmente importa</strong>, sem perder
                                tempo com
                                informações dispersas.
                            </span>
                        </div>
                        <div class="w-[40%] text-center items-center justify-center">
                            <span class="card-text">
                                Com o <strong>InSight</strong>, manter-se atualizado nunca foi tão simples. Oferecemos
                                resumos completos
                                e a opção de <strong>áudio estilo podcast</strong> para quem prefere praticidade.

                                Além disso, você pode receber uma <strong>newsletter diária</strong> com as principais
                                atualizações dos
                                temas que acompanha, <strong>diretamente no seu e-mail</strong>.

                                Simplifique sua rotina de informação com o <strong>InSight</strong> e veja a diferença
                                no seu dia a dia.
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- <Card class="w-full">
            <template #content>

            </template>
        </Card> -->
    </div>
</template>

<script setup lang="ts">
import Card from 'primevue/card';


</script>

<style scoped>
@media (max-width: 768px) {
    .landing-card {
        flex-direction: column;
    }
    .landing-card-text {
        width: 100%;
    }
}
</style>