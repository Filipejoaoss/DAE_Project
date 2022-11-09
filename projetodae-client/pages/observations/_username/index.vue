<template>
  <div>
    <br>
    <h1 class="text-center" v-if="observations != ''">List of Observations</h1>
    <h1 class="text-center" v-if="observations == ''">No Data</h1>
    <b-container v-else class="mt-3 bg-light rounded-lg">
      <b-table id="my-table" :per-page="perPage" :current-page="currentPage" striped over :items="observations" :fields="fields">
        <template v-slot:cell(actions)="row" >
          <b-button size="sm" v-if="$auth.user.sub == row.item.examiner || $auth.user.groups.includes('Administrator') || $auth.user.sub == row.item.patientUsername" :to="`/observations/${$route.params.username}/${row.item.id}/upload`"><b-icon icon="upload" aria-hidden="true"></b-icon> Attach File</b-button>
          <b-button size="sm" v-if="$auth.user.sub == row.item.examiner || $auth.user.groups.includes('Administrator') || $auth.user.sub == row.item.patientUsername" :to="`/observations/${$route.params.username}/${row.item.id}/details`"><b-icon icon="eye-fill" aria-hidden="true"></b-icon> Details</b-button>
        </template>
      </b-table>
    </b-container>
    <b-container v-show="observations != ''" class="pagination justify-content-center">
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
      <nuxt-link :to="`/observations/${this.username}/create`">
        <b-button>Create Observation</b-button>
      </nuxt-link>
      <nuxt-link to="/patients" class="btn float-right">
        <b-button>Back</b-button>
      </nuxt-link>
    </b-container>
  </div>
</template>
<script>
export default {
  middleware: 'observations',
  layout: 'navbar',
  data() {
    return {
      fields: [{key:"id", sortable:true}, {key:"patientUsername", label: "Patient"}, {key:"examiner", label: "Measured by"}, {key:"dateTime.dateTime", label: "Time", sortable: true} , {key:"biomedicDataType.name", label: "Data Type"}, "qualitative", "value"  , "actions"],
      observations: [],
      loggedUser: this.$auth.user.sub,
      userToDelete: null,
      perPage: 5,
      currentPage: 1
    };
  },
  mounted(){
      this.perPage = Math.floor(document.body.clientHeight / 100)
      if(this.perPage < 5){
        this.perPage = 5
      }
  },
  created() {
    this.$axios
      .$get(`/api/observations/${this.$route.params.username}`)
      .then((observations) => {
        this.observations = observations
      });
  },
  computed: {
    username() {
      return this.$route.params.username;
    },
    rows() {
      return this.observations.length
    },
  },
}
</script>

