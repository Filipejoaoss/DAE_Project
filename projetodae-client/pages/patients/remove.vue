<template>
  <div>
    <br>
    <b-container>
      <div>
        <h1>Remove Patient</h1>
        <form @submit.prevent="remove">
          <b-form-select v-model="username" :options="patients"></b-form-select>
        </form>
        <br>
        <b-button type="reset" @click="reset">Reset</b-button>
        <b-button  @click.prevent="remove">Remove</b-button>
        <nuxt-link to="/patients" class="btn float-right">
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
      patients: [],
    }
  },
  created() {
    this.$axios
      .$get("/api/patients")
      .then(response => {
        this.patients = response.map((item) => {
          return {value: item.username, text: item.name}
        })
      });
  },
  methods: {
    remove() {
      this.$axios.$delete('/api/patients/' + this.username)
        .then(() => {
          this.$router.push('/patients')
        })
        .catch((e)=>{
          alert(e)
        })
    }
  }
}
</script>
