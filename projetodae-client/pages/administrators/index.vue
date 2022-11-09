<template>
  <div>
    <br>
    <h1 class="text-center">List of Administrators</h1>
    <b-container class="mt-3 bg-light rounded-lg">
      <b-table id="my-table" :per-page="perPage" :current-page="currentPage" striped over :items="administrators" :fields="fields">
        <template v-slot:cell(actions)="row" >
          <b-button size="sm" :to="`/administrators/${row.item.username}/send-email`"><b-icon icon="envelope-fill" aria-hidden="true"></b-icon> Send email</b-button>
          <b-button size="sm" v-if="loggedUser == row.item.username" :to="`/administrators/${row.item.username}`"><b-icon icon="pencil-square" aria-hidden="true"></b-icon> Edit</b-button>
          <b-button v-if="$auth.user.sub == row.item.username" size="sm" @click="showModal(row.item.username)" ><b-icon icon="trash-fill" aria-hidden="true"></b-icon> Remove</b-button>
        </template>
      </b-table>
    </b-container>
     <b-container v-show="administrators != ''" class="pagination justify-content-center">
      <b-pagination
        v-model="currentPage"
        :total-rows="rows"
        :per-page="perPage"
        first-text="First"
        prev-text="Prev"
        next-text="Next"
        last-text="Last"
        aria-controls="my-table"></b-pagination>
    </b-container>
    <b-container>
      <nuxt-link to="administrators/create">
        <b-button>Create New Administrator</b-button>
      </nuxt-link>
      <nuxt-link to="/" class="btn float-right">
        <b-button>Back</b-button>
      </nuxt-link>
    </b-container>
    <b-modal ref="my-modal" hide-footer title="Confirm Delete">
      <div class="d-block text-center">
        <h4>Are you sure you want to continue ?</h4>
        <p>This account will be deleted and redirected to the login page</p>
      </div>
      <b-button class="mt-3" variant="outline-danger" block @click="removeOk(userToDelete)">Delete</b-button>
    </b-modal>
  </div>
</template>
<script>
export default {
  layout: 'navbar',
  middleware: 'administrator',
  data() {
    return {
      fields: ["username", "name", "contact", "address", "email", "actions"],
      administrators: [],
      loggedUser: this.$auth.user.sub,
      userToDelete: null,
      perPage: 5,
      currentPage: 1
    };
  },
  created() {
    this.$axios
      .$get("/api/administrators")
      .then((administrators) => {
        this.administrators = administrators;
      });
  },
  computed: {
    rows() {
      return this.administrators.length
    }
  },
  methods: {
    removeOk(username) {
      this.$axios.$delete('/api/administrators/' + username)
        .then(() => {

          const index = this.administrators.findIndex(administrator => administrator.username === username)
            if (~index){
              this.administrators.splice(index, 1)
            }
            this.$auth.logout()
            this.$router.push('auth/login')
        })
        .catch((e)=>{
          alert(e)
        })
        this.$refs['my-modal'].hide()
    },
    showModal(username) {
      this.$refs['my-modal'].show()
      this.userToDelete = username
    },
  }
}
</script>

