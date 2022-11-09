<template>
  <div>
    <br>
    <b-container>
      <div>
        <h1>Remove BioMedicDataType</h1>
        <form @submit.prevent="remove">
          <b-form-select v-model="selected" :options="biomedicDataTypes"></b-form-select>
          <!--<div class="mt-3">Selected: <strong>{{ selected }}</strong></div>-->
        </form>
        <br>
        <b-button type="reset" @click="reset">Reset</b-button>
        <b-button @click.prevent="remove">Remove</b-button>
        <nuxt-link to="/biomedicDataTypes" class="btn float-right">
          <b-button>Back</b-button>
        </nuxt-link>
      </div>
    </b-container>
  </div>
</template>
<script>
export default {
  middleware: 'bioMedic',
  layout: 'navbar',
  data() {
    return {
      selected: null,
      biomedicDataTypes: [],
    }
  },
  created() {
    this.$axios
      .$get("/api/biomedicDataTypes")
      .then(response => {
          this.biomedicDataTypes = response.map((item) => {
              return {value: item.id, text: item.name}
          })
      });
  },
  methods: {
    remove() {
      this.$axios.$delete('/api/biomedicDataTypes/' + this.selected)
        .then(() => {
          this.$router.push('/biomedicDataTypes')
        })
        .catch((e)=>{
          alert(e)
        })
    }
  }
}
</script>
