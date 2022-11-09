<template>
  <div>
    <br>
    <h1 class="text-center">List of Prescriptions</h1>
    <b-container class="mt-3 bg-light rounded-lg">
      <b-table id="my-table" :per-page="perPage" :current-page="currentPage" striped over :items="prescriptions" :fields="fields">
        <template #cell(active)="row">
          <span :class="`badge badge-pill badge-${spanType(row.value)}`">{{spanText(row.value)}}</span>
          <b-button v-if="$auth.user.groups.includes('Medic')" size="sm" @click="showModalPrescriptions(row.item.id)"><b-icon icon="trash-fill" aria-hidden="true"></b-icon> Remove</b-button>

        </template>
      </b-table>
    </b-container>
    <b-container v-show="prescriptions != ''" class="pagination justify-content-center">
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

        <b-button  @click="toggle">{{ btnText }}</b-button>

      <nuxt-link to="/" class="btn float-right">
        <b-button>Back</b-button>
      </nuxt-link>
    </b-container>
    <b-modal ref="my-modal" hide-footer title="Confirm Delete">
      <div class="d-block text-center">
        <h4>Are you sure you want to continue ?</h4>
        <p>This account will be deleted</p>
      </div>
      <b-button class="mt-3" variant="outline-danger" block @click="removeOk(prescriptionToDelete)">Delete</b-button>
    </b-modal>
  </div>
</template>
<script>
export default {
  layout: 'navbar',
  data() {
    return {
      fields: ["id", {key:"patientUsername", label:"Patient"}, {key:"startDate.dateTime", label:"Start Date"}, {key:"endDate.dateTime", label:"End Date"}, "title", "description" , "observationId", "active"],
      prescriptions: [],
      prescriptionsRaw: [],
      perPage: 5,
      currentPage: 1,
      prescriptionToDelete: null,
      showActive: false,
    };
  },
  created() {
    this.$axios
      .$get("/api/prescriptions")
      .then((prescriptions) => {
        this.prescriptionsRaw = prescriptions
        this.prescriptions = prescriptions
      });
  },
  computed: {
    rows() {
      return this.prescriptions.length
    },
    btnText(){
      if(this.showActive){
        return "Show All Prescriptions"
      }
      return "Show Current Prescriptions"
    },
  },
  methods: {
    removeOk(id) {
      this.$axios.$delete('/api/prescriptions/' + id)
        .then(() => {
            const index = this.prescriptions.findIndex(prescription => prescription.id === id)
              if (~index){
                this.prescriptions.splice(index, 1)
              }
        })
        .catch((e)=>{
          alert(e)
        })
        this.$refs['my-modal'].hide()
    },
    showModalPrescriptions(id) {
      this.$refs['my-modal'].show()
      this.prescriptionToDelete = id
    },
    isThisActive(p){
      return p.active
    },
    toggle(){
      this.showActive = !this.showActive
      if(this.showActive){
        this.prescriptions = this.prescriptionsRaw.filter(this.isThisActive)
      }else{
        this.prescriptions = this.prescriptionsRaw
      }
    },
    spanType(b){
      if(b){
        return "success"
      }
      return "danger"
    },
    spanText(b){
      if(b){
        return "Active"
      }
      return "Expired"
    }
  }
};
</script>
