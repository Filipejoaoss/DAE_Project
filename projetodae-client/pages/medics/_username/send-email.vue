<template>
  <div>
    <br>
    <b-container>
      <h3>Send an E-mail to Medic {{medic.name}}</h3>
      <br>
      <form @submit.prevent="send">
        <div class="input-group">
          <span class="input-group-text">Subject</span>
          <input class="form-control" v-model="subject" type="text" />
        </div>
        <br>
        <div class="input-group">
          <span class="input-group-text">Message</span>
          <textarea class="form-control" aria-label="With textarea" v-model="message" name="message"></textarea>
        </div>
        <br>
        <b-button @click.prevent="send">Send</b-button>
        <nuxt-link to="/medics" class="btn float-right">
          <b-button>Back</b-button>
        </nuxt-link>
      </form>
    </b-container>
  </div>
</template>
<script>
export default {
  layout: 'navbar',
  data() {
    return {
      medic: {},
      subject: null,
      message: null,
    };
  },
  created() {
    this.$axios.$get(`/api/medics/${this.username}`).then((medic) => {
      this.medic = medic;
    });
  },
  computed: {
    username() {
      return this.$route.params.username;
    },
  },
  methods: {
    send() {
      this.$axios
        .$post(`/api/medics/${this.username}/email`, {
          subject: this.subject,
          message: this.message,
        })
        .then((msg) => {
          this.$toast.success(msg).goAway(1500);
        })
        .catch((error) => {
          this.$toast.error("error sending the e-mail").goAway(3000);
        });
    },
  },
};
</script>
