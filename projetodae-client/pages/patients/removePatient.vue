<template>
  <div>
    <br>
    <h1 class="text-center">List of your Patients</h1>
    <b-container class="mt-3 bg-light rounded-lg">
      <b-table id="my-table" :per-page="perPage" :current-page="currentPage" striped over :items="patients" :fields="fields">
        <template v-slot:cell(actions)="row">
          <b-button @click="showModalPatients(row.item.username)" size="sm"><b-icon icon="dash-circle" aria-hidden="true"></b-icon> Remove</b-button>
        </template>
      </b-table>
    </b-container>
    <b-container v-show="patients != ''" class="pagination justify-content-center">
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
    <b-container v-if="this.$auth.user.groups.includes('Medic') || this.$auth.user.groups.includes('Administrator')">
      <nuxt-link to="/patients" class="btn float-right">
        <b-button>Back</b-button>
      </nuxt-link>
    </b-container>
    <b-modal ref="my-modal" hide-footer title="Confirm Delete">
      <div class="d-block text-center">
        <h4>Are you sure you want to continue ?</h4>
        <p>This account will be deleted</p>
      </div>
      <b-button class="mt-3" variant="outline-danger" block @click="removeOk(userToDelete)">Remove</b-button>
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
      patients: [],
      userToDelete: null,
      perPage: 5,
      currentPage: 1
    };
  },
  created() {
    this.$axios
      .$get("/api/patients")
      .then((patients) => {
        this.patients = patients
      });
  },
  computed: {
    rows() {
      return this.patients.length
    }
  },
  methods: {
    removeOk(username) {
      console.log(username)
      this.$axios.$delete(`/api/medics/${this.$auth.user.sub}/patients/${username}`)
        .then(() => {
          const index = this.patients.findIndex(patient => patient.username === username)
          if (~index){
            this.patients.splice(index, 1)
          }

        })
        .catch((e)=>{
          alert(e)
        })
      this.$refs['my-modal'].hide()
    },
    showModalPatients(username) {
      this.$refs['my-modal'].show()
      this.userToDelete = username
    },
    add(){

    }
  }
};
</script>
