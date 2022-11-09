<template>
  <div>
    <br>
    <b-container>
      <div>
        <h1>Create new BiomedicDataType</h1>
        <form @submit.prevent="create" :disabled="!isFormValid">

          <b-form-group description="The name is required" label="Name of the BioMedicDataType" label-for="name"
                        :invalid-feedback="invalidNameFeedback" :state="isNameValid">
            <b-input v-model.trim="name" required :state="isNameValid" placeholder="BioMedicDataType Name"/>
          </b-form-group>

          <b-form-group description="The description is required" label="Enter a description" label-for="description"
                        :invalid-feedback="invalidDescriptionFeedback" :state="isDescriptionValid">
            <b-input v-model="description" type="text" :state="isDescriptionValid" placeholder="Enter a description"/>
          </b-form-group>

          <b-form-group description="The min value is required" label="Enter then min value here" label-for="minValue"
                        :invalid-feedback="invalidMinValueFeedback" :state="isMinValueValid">
            <b-input v-model="valueMin" type="number" :state="isMinValueValid" placeholder="Enter a min value"/>
          </b-form-group>

          <b-form-group description="The max value is required" label="Enter then max value here" label-for="maxValue"
                        :invalid-feedback="invalidMaxValueFeedback" :state="isMaxValueValid">
            <b-input v-model="valueMax" type="number" :state="isMaxValueValid" placeholder="Enter a max value"/>
          </b-form-group>

          <b-form-group description="The unity is required" label="Enter the unity measure" label-for="unity"
                        :invalid-feedback="invalidUnityFeedback" :state="isEmailValid">
            <b-input v-model="unity" type="text" :state="isUnityValid" placeholder="Enter the unity measure"/>
          </b-form-group>

          <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>
          <b-button type="reset" @click="reset">Reset</b-button>
          <b-button @click.prevent="create" :disabled="!isFormValid">Create</b-button>
          <nuxt-link to="/biomedicDataTypes" class="btn float-right">
            <b-button>Back</b-button>
          </nuxt-link>
        </form>
      </div>
    </b-container>
  </div>
</template>
<script>
export default {
  middleware: 'bioMedic',
  layout: 'navbar',
  data() {
    return {
      name: null,
      description: null,
      valueMin: null,
      valueMax: null,
      unity: null,
      errorMsg: false
    }
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

    isFormValid() {
      if (!this.isNameValid) {
        return false
      }
      return true
    }
  },
  methods: {
    reset() {
      this.errorMsg = false
    },
    create() {
      this.$axios.$post('/api/biomedicDataTypes', {
        name: this.name,
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
