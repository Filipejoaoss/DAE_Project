<template>
  <div>
    <br>
    <b-container class="mt-3 bg-light rounded-lg">
      <b-table id="my-table" :per-page="perPage" :current-page="currentPage" striped over :items="documents" :fields="fields">
        <template v-slot:cell(actions)="row">
          <b-button @click="downloadFile(row.item)"><b-icon icon="download" aria-hidden="true"></b-icon> Download</b-button>
          <b-button v-if="$auth.user.groups.includes('Administrator')" size="sm" @click="remove(row.item)"><b-icon icon="trash-fill" aria-hidden="true"></b-icon> Remove</b-button>
        </template>
      </b-table>
    </b-container>
    <b-container v-show="documents != ''" class="pagination justify-content-center">
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
      fields: ["id", "fileName", "observationId", "actions"],
      documents: [],
      perPage: 5,
      currentPage: 1,
    };
  },
  created() {
    this.$axios
      .$get("/api/documents")
      .then((documents) => {
        this.documents = documents
      });
  },
  computed: {
    rows() {
      return this.documents.length
    }
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

      },
      remove(fileToRemove){
        this.$axios.delete(`/api/documents/${fileToRemove.id}`)
        .then((res)=>{
          if(res.data == true){
            const index = this.documents.findIndex(doc => doc.id === fileToRemove.id)
            if (~index){
              this.documents.splice(index, 1)
            }
            this.$toast.success(`File ${fileToRemove.fileName} was deleted`).goAway(1500)
          }
        })
      }
  }
};
</script>
