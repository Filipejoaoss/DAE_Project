<!--<template>
  <div>
    <br>
    <b-container>
      <h1>Update Patient</h1>
      <b-form-select v-model="username" :options="patients" @change="handler"></b-form-select>
      <br>
      <div class="mt-3"> <b>Patient Username:</b> {{ username }}</div>
      <br>
      <form @submit.prevent="update" v-if="username!=null" :disabled="!isFormValid">
        <b-form-group description="The name is required" label="Enter your name" label-for="name"
                      :invalid-feedback="invalidNameFeedback" :state="isNameValid">
          <b-input v-model.trim="name" required :state="isNameValid" placeholder="Enter your new name"/>
        </b-form-group>

        <b-form-group description="The password is required" label="Enter your password" label-for="password"
                      :invalid-feedback="invalidPasswordFeedback" :state="isPasswordValid">
          <b-input v-model="password" type="password" :state="isPasswordValid" required
                   placeholder="Enter your new password"/>
        </b-form-group>

        <b-form-group description="The contact is required" label="Enter your contact" label-for="contact"
                      :invalid-feedback="invalidContactFeedback" :state="isContactValid">
          <b-input v-model="contact" type="contact" :state="isContactValid" placeholder="Enter your new contact"/>
        </b-form-group>

        <b-form-group description="The address is required" label="Enter your address" label-for="address"
                      :invalid-feedback="invalidAddressFeedback" :state="isAddressValid">
          <b-input v-model="address" type="address" :state="isAddressValid" placeholder="Enter your new address"/>
        </b-form-group>

        <b-form-group description="The email is required" label="Enter your email" label-for="email"
                      :invalid-feedback="invalidEmailFeedback" :state="isEmailValid">
          <b-input ref="email" v-model.trim="email" type="email" :state="isEmailValid" required
                   pattern=".+@mail.pt" placeholder="Enter your new e-mail"/>
        </b-form-group>

        <b-button type="reset">Reset</b-button>
        <b-button @click.prevent="update" :disabled="!isFormValid">Update</b-button>
        <nuxt-link to="/patients" class="btn float-right">
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
      username: null,
      patients: [],
      name: null,
      password: null,
      contact: null,
      address: null,
      email: null,
    }
  },
  created() {
    this.$axios
      .$get("/api/patients")
      .then(response => {
        this.patients = response.map((item) => {
          return {value: item.username, text: item.name }
        })
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
    invalidPasswordFeedback() {
      if (!this.password) {
        return null
      }
      let passwordLen = this.password.length
      if (passwordLen < 8) {
        return 'The password must have at least 8 characters.'
      }
      return ''
    },
    isPasswordValid() {
      if (!this.password) {
        return null
      }
      let passwordLen = this.password.length
      if (passwordLen < 3 || passwordLen > 20) {
        return false
      }
      return true
    },
    invalidContactFeedback() {
      if (!this.contact) {
        return null
      }
      let contactLen = this.contact.length
      if (contactLen !== 9) {
        return 'The contact must have 9 characters.'
      }
      return ''
    },
    isContactValid() {
      if (!this.contact) {
        return null
      }
      let contactLen = this.contact.length
      if (contactLen != 9) {
        return false
      }
      return true
    },
    invalidAddressFeedback() {
      if (!this.address) {
        return null
      }
      let addressLen = this.address.length
      if (addressLen < 4) {
        return 'The address must have at least 4 characters.'
      }
      return ''
    },
    isAddressValid() {
      if (!this.address) {
        return null
      }
      let addressLen = this.address.length
      if (addressLen < 4 || addressLen > 25) {
        return false
      }
      return true
    },
    invalidEmailFeedback() {
      if (!this.email) {
        return null
      }
      return ''
    },
    isEmailValid() {
      if (!this.email) {
        return null
      }
      return this.$refs.email.checkValidity()
    },
    isFormValid() {
      if (!this.isNameValid) {
        return false
      }
      if (!this.isPasswordValid) {
        return false
      }
      if (!this.isContactValid) {
        return false
      }
      if (!this.isAddressValid) {
        return false
      }
      if (!this.isEmailValid) {
        return false
      }
      return true
    }
  },
  methods: {
    handler(){
      this.$axios
        .$get('/api/patients/' + this.username)
        .then(response => {
          this.name = response.name,
            this.password = response.password,
            this.contact = response.contact,
            this.address = response.address,
            this.email = response.email
        });
    },
    update() {
      this.$axios
        .$get('/api/patients/' + this.username)
        .then(response => {
          this.$axios.$put('/api/patients/' + this.username, {
            username: this.username,
            name: this.name,
            password: this.password,
            contact: this.contact,
            address: this.address,
            email: this.email,
          })
            .then(() => {
              this.$router.push('/patients')
            })
            .catch(error => {
              this.errorMsg = error.response.data
            })
        });
    }
  }
}
</script>
-->