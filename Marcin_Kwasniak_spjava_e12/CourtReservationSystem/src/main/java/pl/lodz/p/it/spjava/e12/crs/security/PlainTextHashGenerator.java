package pl.lodz.p.it.spjava.e12.crs.security;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
public class PlainTextHashGenerator implements HashGenerator {

    @Override
    public String generateHash(String input) {
        return input;
    }
}
