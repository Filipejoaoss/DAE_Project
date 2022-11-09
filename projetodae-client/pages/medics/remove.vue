<template>
  <div>
    <br>
    <b-container>
      <div>
        <h1>Remove Medic</h1>
        <form @submit.prevent="remove">
          <b-form-select v-model="username" :options="medics"></b-form-select>
        </form>
        <br>
        <b-button type="reset" @click="reset">Reset</b-button>
        <b-button @click.prevent="remove">Remove</b-button>
        <nuxt-link to="/medics" class="btn float-right">
          <b-button>Back</b-button>
        </nuxt-link>
      </div>
    </b-container>
  </div>
</template>
<script>
export default {
  middleware: 'administrator',
  layout: 'navbar',
  data() {
    return {
      username: null,
      medics: [],
    }
  },
  created() {
    this.$axios
      .$get("/api/medics")
      .then(response => {
        this.medics = response.map((item) => {
          return {value: item.username, text: item.name}
        })
      });
  },
  methods: {
    remove() {
      this.$axios.$delete('/api/medics/' + this.username)
        .then(() => {
          this.$router.push('/medics')
        })
        .catch((e)=>{
          alert(e)
        })
    }
  }
}
</script>
