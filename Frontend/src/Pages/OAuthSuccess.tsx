import { Spinner } from "../components/ui/spinner";


function OAuthSuccess() {

  return (
    <div className="flex flex-col items-center gap-3">

      <Spinner />

      <h1>
        Please wait...
      </h1>

    </div>
  );
}

export default OAuthSuccess;

