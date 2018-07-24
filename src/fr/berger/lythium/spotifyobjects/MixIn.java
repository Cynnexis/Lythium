package fr.berger.lythium.spotifyobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MixIn {

	@JsonIgnore
	abstract String type();
}
