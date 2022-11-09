<template>
  <div>
    <br>
    <h1 class="text-center">List of Medics</h1>
    <b-container class="mt-3 bg-light rounded-lg">
      <b-table id="my-table" :per-page="perPage" :current-page="currentPage" striped over :items="medics" :fields="fields">
        <template v-slot:cell(actions)="row">
          <b-button size="sm" :to="`/medics/${row.item.username}/send-email`"><b-icon icon="envelope-fill" aria-hidden="true"></b-icon> Send email</b-button>
          <b-button size="sm" v-if="$auth.user.sub == row.item.username || $auth.user.groups.includes('Administrator')" :to="`/medics/${row.item.username}`"><b-icon icon="pencil-square" aria-hidden="true"></b-icon> Edit</b-button>
          <b-button v-if="$auth.user.groups.includes('Administrator')" size="sm" @click="showModalMedic(row.item.username)" ><b-icon icon="trash-fill" aria-hidden="true"></b-icon> Remove</b-button>
        </template>
      </b-table>
    </b-container>
    <b-container v-show="medics != ''" class="pagination justify-content-center">
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
    <b-container v-if="this.$auth.user.groups.includes('Administrator')">
      <nuxt-link to="medics/create">
        <b-button>Create New Medic</b-button>
      </nuxt-link>
      <nuxt-link to="/" class="btn float-right">
        <b-button>Back</b-button>
      </nuxt-link>
    </b-container>
    <b-modal ref="my-modal" hide-footer title="Confirm Delete">
      <div class="d-block text-center">
        <h4>Are you sure you want to continue ?</h4>
        <p>This account will be deleted</p>
      </div>
      <b-button class="mt-3" variant="outline-danger" block @click="removeOk(userToDelete)">Delete</b-button>
    </b-modal>
  </div>
</template>
<script>
export default {
  middleware: 'bioMedic',
  layout: 'navbar',
  data() {
    return {
      fields: ["username", "name", "contact", "address", "email", "actions"],
      medics: [],
      userToDelete: null,
      perPage: 5,
      currentPage: 1
    };
  },
  created() {
    this.$axios
      .$get("/api/medics")
      .then((medics) => {
        this.medics = medics;
      });
  },
  computed: {
    rows() {
      return this.medics.length
    }
  },
  methods: {
    removeOk(username) {
      this.$axios.$delete('/api/medics/' + username)
        .then(() => {
            const index = this.medics.findIndex(medic => medic.username === username)
              if (~index){
                this.medics.splice(index, 1)
              }
        })
        .catch((e)=>{
          alert(e)
        })
        this.$refs['my-modal'].hide()
    },
    showModalMedic(username) {
      this.$refs['my-modal'].show()
      this.userToDelete = username
    },
  }
};
</script>
<style>
  .tableBack {
  }
</style>
