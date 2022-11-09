<template>
  <div>
    <br>
    <b-container>
        <h1>Update a BiomedicDataType</h1>
        <form @submit.prevent="update" :disabled="!isFormValid">
            <b-form-group description="The name is required" label="Enter the name" label-for="name"
                        :invalid-feedback="invalidNameFeedback" :state="isNameValid">
            <b-input v-model="name" type="text" :state="isNameValid" placeholder="Enter the name"/>
            </b-form-group>

            <b-form-group description="The description is required" label="Enter a description" label-for="description"
                        :invalid-feedback="invalidDescriptionFeedback" :state="isDescriptionValid">
            <b-input v-model="description" type="text" :state="isDescriptionValid" placeholder="Enter a description"/>
            </b-form-group>

            <b-form-group description="The min value is required" label="Enter then min value here" label-for="minValue">
            <b-input v-model="valueMin" type="number" placeholder="Enter a min value"/>
            </b-form-group>

            <b-form-group description="The max value is required" label="Enter then max value here" label-for="maxValue">
            <b-input v-model="valueMax" type="number" placeholder="Enter a max value"/>
            </b-form-group>

            <b-form-group description="The unity is required" label="Enter the unity measure" label-for="unity"
                        :invalid-feedback="invalidUnityFeedback" :state="isUnityValid">
            <b-input v-model="unity" type="text" :state="isUnityValid" placeholder="Enter the unity measure"/>
            </b-form-group>

            <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>
            <b-button @click="reset">Reset</b-button>
            <b-button @click.prevent="update" :disabled="!isFormValid">Update</b-button>
            <nuxt-link to="/biomedicDataTypes" class="btn float-right">
            <b-button>Back</b-button>
            </nuxt-link>
        </form>
    </b-container>
  </div>
</template>
<script>
export default {
  middleware: 'bioMedic',
  layout: 'navbar',
  data() {
    return {
      biomedicDataType: {},
      id: null,
      name: null,
      description: null,
      valueMin: null,
      valueMax: null,
      unity: null,
    }
  },
  created() {
    this.$axios
      .$get(`/api/biomedicDataTypes/${this.$route.params.id}`)
      .then(response => {
        this.biomedicDataType = response || {}
        this.name = response.name
        this.description = response.description
        this.valueMin = response.valueMin
        this.valueMax = response.valueMax
        this.unity = response.unity
      });
  },
  computed: {
    invalidNameFeedback() {
      if (!this.name) {
        return null
      }
      let nameLen = this.name.length
      if (nameLen < 2) {
        return 'The name must have at least 2 characters.'
      }
      return ''
    },
    isNameValid() {
      if (!this.name) {
        return null
      }
      let nameLen = this.name.length
      if (nameLen < 3 || nameLen > 25) {
        return false
      }
      return true
    },
    invalidDescriptionFeedback() {
      if (!this.description) {
        return null
      }
      let descLen = this.description.length
      if (descLen < 2) {
        return 'The description must have at least 2 characters.'
      }
      return ''
    },
    isDescriptionValid() {
      if (!this.description) {
        return null
      }
      let descLen = this.description.length
      if (descLen < 3) {
        return false
      }
      return true
    },
     invalidUnityFeedback() {
      if (!this.unity) {
        return null
      }
      let unityLen = this.unity.length
      if (unityLen < 1) {
        return 'Unity must have at least 1 character.'
      }
      return ''
    },
    isUnityValid() {
      if (!this.unity) {
        return null
      }
      let unityLen = this.unity.length
      if (unityLen < 2) {
        return false
      }
      return true
    },
    isFormValid() {
      if (!this.isDescriptionValid) {
        return false
      }
      if (!this.isUnityValid) {
        return false
      }
      return true
    }
  },
  methods: {
   
    update() {
        this.$axios.$put('/api/biomedicDataTypes/' + this.biomedicDataType.id, {
            id: this.biomedicDataType.id,
            description: this.description,
            valueMin: this.valueMin,
            valueMax: this.valueMax,
            unity: this.unity
            })
            .then(() => {
            this.$router.push('/biomedicDataTypes')
            })
            .catch(error => {
            this.errorMsg = error.response.data
        })
    }
  }
}
</script>
