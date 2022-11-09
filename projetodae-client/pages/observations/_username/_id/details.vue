<template>
  <div>
    <br>
    <h1 class="text-center"></h1>
        <b-card class="container text-left p-0" header="Observation Details">
            <b-card-text>
                <p><strong>Observation ID: </strong>{{ observation.id }}</p>
                <p><strong>Name: </strong>{{ observation.patientUsername }}</p>
                <p><strong>Examiner: </strong>{{ observation.examiner }}</p>
                <p><strong>Date: </strong>{{ obsDateTime}}</p>
                <p><strong>BioMedicDataType: </strong>{{ biomedicDataTypeName }} </p>
                <p><strong>Qualitative: </strong>{{ observation.qualitative }}</p>
                <p><strong>Value: </strong>{{ observation.value }}</p>
            </b-card-text>
        </b-card>

    <b-container class="bv-example-row p-0">
    <b-row>
        <b-col>
            <b-card v-if="prescriptions == ''" class="mt-3">
                <h1>No prescriptions Yet</h1>
            </b-card>
            <b-container v-else class="mt-3 bg-light rounded-lg p-0">
                <b-table id="my-table" :per-page="perPagePrescriptions" :current-page="currentPagePrescriptions" striped over :items="prescriptions" :fields="prescriptionsFields">
                <template v-slot:cell(actions)="row" >
                </template>
            </b-table>
            </b-container>
            <b-container v-show="prescriptions != ''" class="pagination justify-content-center">
                <b-pagination
                    v-model="currentPagePrescriptions"
                    :total-rows="rowsPrescriptions"
                    :per-page="perPagePrescriptions"
                    first-text="First"
                    prev-text="Prev"
                    next-text="Next"
                    last-text="Last"
                    aria-controls="my-table"></b-pagination>
            </b-container>
        </b-col>
        <b-col>
            <b-card v-if="filesUploaded == ''" class="mt-3 text-center" >
                <h1>No Files Uploaded</h1>
                <b-icon style="width: 45px; height: 45px;" icon="file-earmark-arrow-up" aria-hidden="true"></b-icon>
            </b-card>
            <b-container v-else class="mt-3 bg-light rounded-lg p-0">
                <b-table id="my-table" :per-page="perPageFiles" :current-page="currentPageFiles" striped over :items="filesUploaded" :fields="fileFields">
                    <template v-slot:cell(actions)="row" >
                        <b-button size="sm" @click="downloadFile(row.item)" ><b-icon icon="cloud-download" aria-hidden="true"></b-icon> Download</b-button>
                    </template>
                </b-table>
            </b-container>
            <b-container v-show="filesUploaded != ''" class="pagination justify-content-center">
                <b-pagination
                    v-model="currentPageFiles"
                    :total-rows="rowsFiles"
                    :per-page="perPageFiles"
                    first-text="First"
                    prev-text="Prev"
                    next-text="Next"
                    last-text="Last"
                    aria-controls="my-table"></b-pagination>
            </b-container>
        </b-col>
    </b-row>
    </b-container>
    <b-container>
      <nuxt-link v-if="$auth.user.groups.includes('Medic')" :to="`/prescriptions/${this.username}/${this.observation.id}/create`">
        <b-button>Create Prescription</b-button>
      </nuxt-link>
      <nuxt-link to="/" class="btn float-right">
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
      biomedicDataTypeName: '',
      obsDateTime: '',
      observation: {},
      prescriptions: [],
      filesUploaded: [],
      loggedUser: this.$auth.user.sub,
      userToDelete: null,
      perPagePrescriptions: 5,
      currentPagePrescriptions: 1,
      perPageFiles: 5,
      currentPageFiles: 1,
      prescriptionsFields: [ {key:"patientUsername", label:"Patient"}, {key:"startDate.dateTime", label:"Start Date"}, {key:"endDate.dateTime", label:"End Date"}, "title", "description" , "observationId"],
      fileFields: [{key:"id", label:"Doc.no"},'fileName' ,'actions']
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
      .$get(`/api/observations/${this.$route.params.username}/${this.$route.params.id}`)
      .then((observation) => {
        this.observation = observation
        this.biomedicDataTypeName = observation.biomedicDataType.name
        this.obsDateTime = observation.dateTime.day  + '/' + observation.dateTime.month + '/' + observation.dateTime.year + ' at ' + observation.dateTime.hour + 'h ' + observation.dateTime.minute + 'm'
        
      }).catch((error ) => {
          if (error.response.status === 403) {
            return this.$router.push('/patients')           
          } 
      })
    this.$axios
      .$get(`/api/prescriptions/observations/${this.$route.params.id}`)
      .then((prescriptions) => {
        this.prescriptions = prescriptions
      });
    this.$axios
        .$get('/api/documents/')
        .then((filesUploaded) => {
        this.filesUploaded = filesUploaded
    });
  },
  computed: {
    username() {
      return this.$route.params.username;
    },
    rowsPrescriptions() {
      return this.prescriptions.length
    },
    rowsFiles() {
      return this.filesUploaded.length
    },
  },
  methods: {
      downloadFile(fileToDownload){
        const documentId = fileToDownload.id
        this.$axios.$get('/api/documents/' + documentId, { responseType: 'arraybuffer'})
            .then(file => {
                const url = window.URL.createObjectURL(new Blob([file]))
                const link = document.createElement('a')
                link.href = url
                link.setAttribute('download', fileToDownload.fileName)
                document.body.appendChild(link)
                link.click()
            })
      }
  }
}
</script>

