<template>
  <div>
    <br>
    <h1 class="text-center">List of BiomedicDataTypes</h1>
    <b-container class="mt-3 bg-light rounded-lg">
      <b-table id="my-table" :per-page="perPage" :current-page="currentPage" striped over :items="biomedicDataTypes" :fields="fields">
        <template v-slot:cell(actions)="row" >
          <nuxt-link v-if="$auth.user.groups.includes('bioMedic')"
            class="btn btn-link"
            :to="`/biomedicDataTypes/${row.item.id}`">Update</nuxt-link>
            <b-button v-if="$auth.user.groups.includes('Administrator') || $auth.user.groups.includes('Medic')"  size="sm" @click="showModalBio(row.item.id)" ><b-icon icon="trash-fill" aria-hidden="true"></b-icon> Remove</b-button>
        </template>
      </b-table>
    </b-container>
    <b-container v-show="biomedicDataTypes != ''" class="pagination justify-content-center">
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
    <b-container v-if="!this.$auth.user.groups.includes('Patient')">
      <nuxt-link to="biomedicDataTypes/create">
        <b-button>Create New BiomedicDataType</b-button>
      </nuxt-link>
      <nuxt-link to="/" class="btn float-right">
        <b-button>Back</b-button>
      </nuxt-link>
    </b-container>
    <b-modal ref="my-modal" hide-footer title="Confirm Delete">
      <div class="d-block text-center">
        <h4>Are you sure you want to continue ?</h4>
        <p>This BioMedicDataType will be deleted</p>
      </div>
      <b-button class="mt-3" variant="outline-danger" block @click="removeOk(itemID)">Delete</b-button>
    </b-modal>
  </div>
</template>
<script>
export default {
  layout: 'navbar',
  data() {
    return {
      showModal: false,
      fields: ["name", "description", "valueMin", "valueMax", "unity", "actions"],
      biomedicDataTypes: [],
      itemID: null,
      perPage: 5,
      currentPage: 1
    };
  },
  created() {
    this.$axios
      .$get("/api/biomedicDataTypes")
      .then((biomedicDataTypes) => {
        this.biomedicDataTypes = biomedicDataTypes;
      });
  },
  computed: {
    rows() {
      return this.biomedicDataTypes.length
    }
  },
  methods: {
    removeOk(id) {
      this.$axios.$delete('/api/biomedicDataTypes/' + id)
        .then(() => {
            const index = this.biomedicDataTypes.findIndex(biomedicDataType => biomedicDataType.id === id)
              if (~index){
              this.biomedicDataTypes.splice(index, 1)
              }
        })
        .catch((e)=>{
          alert(e)
        })
        this.$refs['my-modal'].hide()
    },
    showModalBio(id) {
      this.$refs['my-modal'].show()
      this.itemID = id
    },
  }
};
</script>

