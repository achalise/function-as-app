- docker image build using buildpack
- define a new profile to use kafka from confluent cloud - done
- define K8S yamls for service and deployment

- Build image - `./gradlew bootBuildImage` (it will take a long time (~20 minutes) for the first build)
- `docker run -e "SPRING_PROFILES_ACTIVE=cf" -p 8080:8080 eventdriven:0.0.1-SNAPSHOT` to run locally using profile to use confluent
- `docker run --platform linux/x86_64 -e "SPRING_PROFILES_ACTIVE=cf" -p 8080:8080 eventdriven:0.0.1-SNAPSHOT` 
- `docker run -e "SPRING_PROFILES_ACTIVE=cf" -p 8080:8080 eventdriven:0.0.1-SNAPSHOT`
  docker run -e "SPRING_PROFILES_ACTIVE=cf" -p 8080:8080 achalise/eventdriven:0.1

kubectl run -i --tty --rm debug --image=busybox --restart=Never -n rebates-it1 -- sh

To build image for amd platform:
`docker buildx build --push --platform linux/amd64 -t achalise/eventdriven:0.2 -f Dockerfile .`

This is the API part of the application which exposes and endpoint for users to submit to claim.
For simplicity, MongoDB is used, populate the connection details for Mongo in `.envrc`.  Kafka is started
locally via `compose.yaml`.

To start the application, `./gradlew bootRun`