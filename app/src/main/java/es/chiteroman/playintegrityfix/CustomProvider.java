package es.chiteroman.playintegrityfix;

import java.security.Provider;

public final class CustomProvider extends Provider {

    public CustomProvider(Provider provider) {
        super(provider.getName(), provider.getVersion(), provider.getInfo());
        putAll(provider);
        put("KeyStore.AndroidKeyStore", CustomKeyStoreSpi.class.getName());

        EntryPoint.LOG("Loading new provider");
        put("KeyPairGenerator.EC", CustomKeyStoreKeyPairGeneratorSpi.EC.class.getName());
        put("KeyPairGenerator.RSA", CustomKeyStoreKeyPairGeneratorSpi.RSA.class.getName());
        put("KeyPairGenerator.OLDEC", provider.get("KeyPairGenerator.EC"));
        put("KeyPairGenerator.OLDRSA", provider.get("KeyPairGenerator.RSA"));
    }

    @Override
    public synchronized Service getService(String type, String algorithm) {
        EntryPoint.LOG(String.format("Service: '%s' | Algorithm: '%s'", type, algorithm));

        Thread t = new Thread(EntryPoint::spoofFields);
        t.setDaemon(true);
        t.start();

        return super.getService(type, algorithm);
    }
}
