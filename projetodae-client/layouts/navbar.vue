<template>
  <div>
  <b-navbar id="navID" toggleable="lg" type="light">
    <b-navbar-brand href="/"><b-icon icon="heart-half" aria-hidden="true"></b-icon> MediCare</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav v-if="this.$auth.user.groups.includes('Patient')">
        <b-nav-item href="/biomedicDataTypes">BiomedicDataTypes</b-nav-item>
        <b-nav-item :to="`/observations/${$auth.user.sub}`">Observations</b-nav-item>
        <b-nav-item href="/prescriptions">Prescriptions</b-nav-item>
      </b-navbar-nav>
      <b-navbar-nav v-if="this.$auth.user.groups.includes('Medic')">
        <b-nav-item href="/patients">Patients</b-nav-item>
        <b-nav-item href="/medics">Medics</b-nav-item>
        <b-nav-item href="/biomedicDataTypes">BiomedicDataTypes</b-nav-item>
        <b-nav-item href="/prescriptions">Prescriptions</b-nav-item>
      </b-navbar-nav>
      <b-navbar-nav v-if="this.$auth.user.groups.includes('Administrator')">
        <b-nav-item href="/patients">Patients</b-nav-item>
        <b-nav-item href="/medics">Medics</b-nav-item>
        <b-nav-item href="/administrators">Administrators</b-nav-item>
        <b-nav-item href="/biomedicDataTypes">BiomedicDataTypes</b-nav-item>
        <b-nav-item href="/prescriptions">Prescriptions</b-nav-item>
      </b-navbar-nav>

      <!-- Right aligned nav items -->
      <b-navbar-nav class="ml-auto">
        <b-nav-item-dropdown v-if="$auth.loggedIn" right>
          <!-- Using 'button-content' slot -->
          <template #button-content>
            <em><b-icon icon="person-circle" aria-hidden="true"></b-icon> {{ $auth.user.sub }}</em>
          </template>
          <b-dropdown-item v-if="this.$auth.user.groups.includes('Patient')" :to="`/patients/${$auth.user.sub}`">Profile</b-dropdown-item>
          <b-dropdown-item v-if="this.$auth.user.groups.includes('Medic')" :to="`/medics/${$auth.user.sub}`">Profile</b-dropdown-item>
          <b-dropdown-item v-if="this.$auth.user.groups.includes('Administrator')" :to="`/administrators/${$auth.user.sub}`">Profile</b-dropdown-item>
          <b-dropdown-item @click.prevent="signOut">Log Out</b-dropdown-item>
        </b-nav-item-dropdown>
        <li class="nav-item" v-else>
          <nuxt-link class="nav-link" to="/auth/login">Sign In</nuxt-link>
        </li>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
  <Nuxt />
</div>
</template>
<script>
export default {
  methods: {
    signOut() {
      this.$auth.logout()
      this.$router.push('auth/login')
    }
  },
}
</script>

<style scoped>
  #navID{
    box-shadow: -2px 5px 28px -15px rgba(0,0,0,0.75);
    -webkit-box-shadow: -2px 5px 28px -15px rgba(0,0,0,0.75);
    -moz-box-shadow: -2px 5px 28px -15px rgba(0,0,0,0.75);
  }
</style>
