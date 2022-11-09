<template>
  <div>
    <br>
    <h1 class="text-center">List of Patients</h1>
    <b-container class="mt-3 bg-light rounded-lg">
      <b-table id="my-table" :per-page="perPage" :current-page="currentPage" striped over :items="patients" :fields="fields">
        <template v-slot:cell(actions)="row">
          <b-button size="sm" :to="`/patients/${row.item.username}/send-email`"><b-icon icon="envelope-fill" aria-hidden="true"></b-icon> Send email</b-button>
          <b-button size="sm" :to="`/observations/${row.item.username}`"><b-icon icon="eye-fill" aria-hidden="true"></b-icon> Observations</b-button>
          <b-button v-if="$auth.user.groups.includes('Administrator')" size="sm" :to="`/patients/${row.item.username}`"><b-icon icon="pencil-square" aria-hidden="true"></b-icon> Edit</b-button>
          <b-button v-if="$auth.user.groups.includes('Administrator')" size="sm" @click="showModalPatients(row.item.username)"><b-icon icon="trash-fill" aria-hidden="true"></b-icon> Remove</b-button>
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
      <nuxt-link to="patients/create">
        <b-button>Create New Patient</b-button>
      </nuxt-link>
      <nuxt-link to="patients/addPatient">
        <b-button>Add Patient</b-button>
      </nuxt-link>
      <nuxt-link to="patients/removePatient">
        <b-button>Remove Patient</b-button>
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
      this.$axios.$delete('/api/patients/' + username)
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
  }
};
</script>
