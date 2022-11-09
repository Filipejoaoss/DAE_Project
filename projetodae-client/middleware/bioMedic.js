export default function({$auth,redirect}){
    if(!$auth.user.groups.includes("Administrator") || !$auth.user.groups.includes("Medic")){
        if($auth.user.groups.includes("Patient")){
            return redirect('/patients/' + $auth.user.sub)
        }
    }
}