<template>
  <div>
    <br>
    <b-container>
      <div>
        <h1>Remove Administrator</h1>
        <form @submit.prevent="remove">
         <b-form-select v-model="username" :options="administrators"></b-form-select>
        </form>
        <br>
        <b-button type="reset" @click="reset">Reset</b-button>
        <b-button @click.prevent="remove">Remove</b-button>
        <nuxt-link to="/administrators" class="btn float-right">
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
      administrators: [],
    }
  },
  created() {
    this.$axios
      .$get("/api/administrators")
      .then(response => {
        this.administrators = response.map((item) => {
          return {value: item.username, text: item.name}
        })
      });
  },
  methods: {
    remove() {
      this.$axios.$delete('/api/administrators/' + this.username)
        .then(() => {
          this.$router.push('/administrators')
        })
        .catch((e)=>{
          alert(e)
        })
    }
  }
}
</script>
