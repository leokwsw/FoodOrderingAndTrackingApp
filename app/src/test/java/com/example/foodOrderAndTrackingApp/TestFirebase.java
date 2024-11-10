package com.example.foodOrderAndTrackingApp;

public class TestFirebase extends AndroidTestCase {
    private static Logger logger = LoggerFactory.getLogger(TestFirebase.class);

    private CountDownLatch authSignal = null;
    private FirebaseAuth auth;

    @Override
    public void setUp() throws InterruptedException {
        authSignal = new CountDownLatch(1);
        Firebase.setAndroidContext(mContext); //initializeFireBase(context);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword("urbi@orbi.it", "12345678").addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {

                            final AuthResult result = task.getResult();
                            final FirebaseUser user = result.getUser();
                            authSignal.countDown();
                        }
                    });
        } else {
            authSignal.countDown();
        }
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        if(auth != null) {
            auth.signOut();
            auth = null;
        }
    }

    @Test
    public void testWrite() throws InterruptedException {
        final CountDownLatch writeSignal = new CountDownLatch(1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Do you have data? You'll love Firebase. - 3")
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        writeSignal.countDown();
                    }
                });

        writeSignal.await(10, TimeUnit.SECONDS);
    }
}
