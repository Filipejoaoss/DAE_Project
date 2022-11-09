<template>
  <div class="container">
    <br>
    <h1 class="text-center">Welcome to MediCare</h1>
    <br>
    <div>
      <div class="container">
        <b-card-group deck>
          <b-card v-if="this.$auth.user.groups.includes('Administrator') || this.$auth.user.groups.includes('Medic')" bg-variant="secondary" text-variant="white" header="Patients" class="text-center">
            <b-card-text>
              <nuxt-link to="/patients">
                <b-button><b-icon icon="box-arrow-in-up-right" aria-hidden="true"></b-icon> Display List of Patients</b-button>
              </nuxt-link>
            </b-card-text>
          </b-card>
          <b-card v-if="this.$auth.user.groups.includes('Administrator') || this.$auth.user.groups.includes('Medic')" bg-variant="secondary" text-variant="white" header="Medics" class="text-center">
            <b-card-text>
              <nuxt-link to="/medics">
                <b-button><b-icon icon="box-arrow-in-up-right" aria-hidden="true"></b-icon> Display List of Medics</b-button>
              </nuxt-link>
            </b-card-text>
          </b-card>
          <b-card v-if="this.$auth.user.groups.includes('Administrator')" bg-variant="secondary" text-variant="white" header="Administrators" class="text-center">
            <b-card-text>
              <nuxt-link to="/administrators">
                <b-button><b-icon icon="box-arrow-in-up-right" aria-hidden="true"></b-icon> Display List of Administrators</b-button>
              </nuxt-link>
            </b-card-text>
          </b-card>
        </b-card-group>
      </div>
      <div class="container">
        <div class="mt-3">
          <b-card-group deck>
            <b-card v-if="this.$auth.user.groups.includes('Administrator') || this.$auth.user.groups.includes('Medic')" bg-variant="secondary" text-variant="white" header="BiomedicDataTypes" class="text-center">
              <b-card-text>
                <nuxt-link to="/biomedicDataTypes">
                  <b-button><b-icon icon="box-arrow-in-up-right" aria-hidden="true"></b-icon> Display List of BiomedicDataTypes</b-button>
                </nuxt-link>
              </b-card-text>
            </b-card>
            <b-card v-if="this.$auth.user.groups.includes('Patient')" bg-variant="secondary" text-variant="white" header="Observations" class="text-center">
              <b-card-text>
                <nuxt-link :to="`/observations/${$auth.user.sub}`">
                  <b-button><b-icon icon="box-arrow-in-up-right" aria-hidden="true"></b-icon> Display List of Observations</b-button>
                </nuxt-link>
              </b-card-text>
            </b-card>
            <b-card bg-variant="secondary" text-variant="white" header="Prescriptions" class="text-center">
              <b-card-text>
                <nuxt-link to="/prescriptions">
                  <b-button><b-icon icon="box-arrow-in-up-right" aria-hidden="true"></b-icon> Display List of Prescriptions</b-button>
                </nuxt-link>
              </b-card-text>
            </b-card>
          </b-card-group>
        </div>
      </div>
    </div>
  </div>
</template>
<script>

export default {
  layout: 'navbar'
}
</script>
<style>
  .principal{
    margin-left: 250px;
  }
  .border{
    justify-content: center;
    display: inline-block;
    text-align: center;
    margin-top: 3px;
    color: white;
    background: #3b3b3b;
    border-style: solid;
    width: 350px;
    height: 120px;
  }

  body {
    height: 100vh;
    /* fallback for old browsers */
    /* Chrome 10-25, Safari 5.1-6 */
    background: -webkit-linear-gradient(to right, #13b9a3, rgb(0, 255, 191));
    /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
    background-color: #08AEEA;
    background-image: linear-gradient(0deg, #08AEEA 0%, #2AF598 100%);

  }
</style>
