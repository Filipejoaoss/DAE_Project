<template>
  <div>
    <br>
    <b-container>
      <div>
        <h1>Create new Medic</h1>
        <form @submit.prevent="create" :disabled="!isFormValid">
          <b-form-group id="username" description="The username is required" label="Enter your username"
                        label-for="username" :invalid-feedback="invalidUsernameFeedback" :state="isUsernameValid">
            <b-input id="username" v-model.trim="username" :state="isUsernameValid" trim></b-input>
          </b-form-group>

          <b-form-group description="The name is required" label="Enter your name" label-for="name"
                        :invalid-feedback="invalidNameFeedback" :state="isNameValid">
            <b-input v-model.trim="name" required :state="isNameValid" placeholder="Enter your name"/>
          </b-form-group>

          <b-form-group description="The password is required" label="Enter your password" label-for="password"
                        :invalid-feedback="invalidPasswordFeedback" :state="isPasswordValid">
            <b-input v-model="password" type="password" :state="isPasswordValid" required
                     placeholder="Enter your password"/>
          </b-form-group>

          <b-form-group description="The contact is required" label="Enter your contact" label-for="contact"
                        :invalid-feedback="invalidContactFeedback" :state="isContactValid">
            <b-input v-model="contact" type="contact" :state="isContactValid" placeholder="Enter your contact"/>
          </b-form-group>

          <b-form-group description="The address is required" label="Enter your address" label-for="address"
                        :invalid-feedback="invalidAddressFeedback" :state="isAddressValid">
            <b-input v-model="address" type="address" :state="isAddressValid" placeholder="Enter your address"/>
          </b-form-group>

          <b-form-group description="The email is required" label="Enter your email" label-for="email"
                        :invalid-feedback="invalidEmailFeedback" :state="isEmailValid">
            <b-input ref="email" v-model.trim="email" type="email" :state="isEmailValid" required
                     pattern=".+@mail.pt" placeholder="Enter your e-mail"/>
          </b-form-group>

          <p class="text-danger" v-show="errorMsg">{{ errorMsg }}</p>

          <b-button type="reset" @click="reset">Reset</b-button>
          <b-button @click.prevent="create" :disabled="!isFormValid">Create</b-button>
          <nuxt-link to="/medics" class="btn float-right">
            <b-button>Back</b-button>
          </nuxt-link>
        </form>
      </div>
    </b-container>
  </div>
</template>
<script>
export default {
  middleware: 'administrator',
  layout: 'navbar',
  data() {
    return {
      username: null,
      name: null,
      password: null,
      contact: null,
      address: null,
      email: null,
      errorMsg: false
    }
  },
  computed: {
    invalidUsernameFeedback() {
      if (!this.username) {
        return null
      }
      let usernameLen = this.username.length
      if (usernameLen < 3 || usernameLen > 15) {
        return 'The username must be between [3, 15] characters.'
      }
      return ''
    },
    isUsernameValid() {
      if (this.invalidUsernameFeedback === null) {
        return null
      }
      return this.invalidUsernameFeedback === ''
    },
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
      if (!this.isUsernameValid) {
        return false
      }
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
    reset() {
      this.errorMsg = false
    },
    create() {
      this.$axios.$post('/api/medics', {
        username: this.username,
        name: this.name,
        password: this.password,
        contact: this.contact,
        address: this.address,
        email: this.email
      })
        .then(() => {
          this.$router.push('/medics')
        })
        .catch(error => {
          this.errorMsg = error.response.data
        })
    }
  }
}
</script>
