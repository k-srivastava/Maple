#version 150

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColor;
uniform float shineDamping;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void)
{
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    vec3 unitCameraVector = normalize(toCameraVector);

    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float brightness = dot(unitNormal, unitLightVector);
    brightness = max(brightness, 0.15);
    vec3 diffusion = brightness * lightColor;

    float specularFactor = dot(reflectedLightDirection, unitCameraVector);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamping);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

    vec4 textureColor = texture(modelTexture, pass_textureCoordinates);
    if (textureColor.a < 0.5) {
        discard;
    }

    out_Color = vec4(diffusion, 1.0) * textureColor + vec4(finalSpecular, 1.0);
    out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}
