<template>
    <b-container class="mt-3">
		<div class="container">
			<b-card
				title="Upload a File"
				img-alt="Image"
				img-top
				tag="article"
				style="max-width: 20rem;'"
				class="mb-2 mx-auto text-center"
				>
				<b-card-text class="text-center">
					<b-icon style="width: 120px; height: 120px;" icon="file-earmark-arrow-up" aria-hidden="true"></b-icon>
				</b-card-text>
				 <form @submit.prevent="upload">
					<b-form-file
						v-model="file"
						:state="hasFile"
						placeholder="Click me or drop file here"
						drop-placeholder="Drop file here..."
						class="text-left"
					></b-form-file>
					<div class="mt-3">Selected file: {{ file ? file.name : "" }}</div>
					<b-button type="submit" :disabled="!hasFile">Upload</b-button>
					<b-button :to="`/observations/${this.patient}`">Back</b-button>
				</form>
 			 </b-card>
		</div>
    </b-container>
</template>
<script>
export default {
	layout: 'navbar',
	data() {
		return {
			username: this.$auth.user.sub,
			patient: this.$route.params.username,
			file: null,
			observationId: this.$route.params.id
		};
	},
	computed: {
		hasFile() {
			return this.file != null;
		},
		formData() {
			let formData = new FormData();
			formData.append("username", this.username);
			if (this.file) {
				formData.append("file", this.file);
			}
			return formData;
		},
	},
	methods: {
		upload() {
			if (!this.hasFile) {
				return;
			}
			let promisse = this.$axios.$post(
				`/api/documents/observations/${this.observationId}/upload`,
				this.formData,
				{
					headers: {
						"Content-Type": "multipart/form-data",
					},
				}
			);
			promisse.then(() => {
				this.$toast.success("File uploaded successfully").goAway(3000);
				this.$router.push('/observations/' + this.patient)
			});
			promisse.catch(() => {
				this.$toast.error("Error uploading file").goAway(3000);
			});
		},
	},
};
</script>