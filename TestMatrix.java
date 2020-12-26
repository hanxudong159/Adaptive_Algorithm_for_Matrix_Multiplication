import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;

public class TestMatrix {
    Matrix t1 = new Matrix(3,3);
    Matrix t2 = new Matrix(3,3);
    @Before
    public void creat(){
        t1.setValueAt(1,1,1);
        t1.setValueAt(1,2,2);
        t1.setValueAt(1,3,3);
        t1.setValueAt(2,1,4);
        t1.setValueAt(2,2,5);
        t1.setValueAt(2,3,6);
        t1.setValueAt(3,1,7);
        t1.setValueAt(3,2,8);
        t1.setValueAt(3,3,9);
        t2 = t1;
    }

    @Test
    public void test1() {
        /*
        t1.show();
        t2.show();
        System.out.println(t1.getValueAt(2,1));
*/
        assertEquals(t1.getMatrixAt(1,1,1,1).toString(),"1.0\n");
        assertEquals(t1.getMatrixAt(1,1,2,2).toString(),"1.0,2.0\n4.0,5.0\n");
        assertEquals(t1.getMatrixAt(1,2,3,3).toString(),"2.0,3.0\n5.0,6.0\n8.0,9.0\n");
        assertEquals(4.0,t1.getValueAt(2,1),0.001);
        assertEquals("30.0,36.0,42.0\n66.0,81.0,96.0\n102.0,126.0,150.0\n",Matrix.square_matrix_multiply(t1,t2).toString());
        double[] tmpans = new double[9];
        Matrix.square(tmpans,t1.getValues(),t2.getValues(),3);
        assertEquals("30.0,36.0,42.0\n66.0,81.0,96.0\n102.0,126.0,150.0\n",new Matrix(3,3,tmpans).toString());
        assertEquals("30.0,36.0,42.0\n66.0,81.0,96.0\n102.0,126.0,150.0\n",Matrix.strassen_multiply(t1,t2).toString());
    }
    @Test
    public void test2(){
        Matrix t3 = Matrix.RandomMatrix(100,100);
        t3.show();
    }
    @Test
    public void test3(){
        Matrix a1 = t1.partitionMatrix(1);
        Matrix a2 = t1.partitionMatrix(2);
        Matrix a3 = t1.partitionMatrix(3);
        Matrix a4 = t1.partitionMatrix(4);
        a1.show();
        a2.show();
        a3.show();
        a4.show();

        Matrix t3 = Matrix.RandomMatrix(8,8);
        t3.show();
        Matrix b1 = t3.partitionMatrix(1);
        Matrix b2 = t3.partitionMatrix(2);
        Matrix b3 = t3.partitionMatrix(3);
        Matrix b4 = t3.partitionMatrix(4);
        b1.show();
        b2.show();
        b3.show();
        b4.show();

        Matrix A = Matrix.mergeMatrix(a1,a2,a3,a4);
        A.show();
        Matrix B = Matrix.mergeMatrix(b1,b2,b3,b4);
        B.show();
        //assertEquals(t1.toString(),A.toString());
        assertEquals(t3.toString(),B.toString());
    }

    @Test
    public void testG(){
        Matrix.RandomMatrix(2000,2000);
    }

    @Test
    public void testFile() {
        try {
            Matrix.calibrate_crossover_point();
        }catch (Exception e){

        }
        Matrix.adaptive_multiply(Matrix.RandomMatrix(50,50),Matrix.RandomMatrix(50,50));
    }

    @Test
    public void testCrossPoint(){
        for (int i = 33; i < 3000; i+=32) {
            Matrix.testmultiply(i);
        }
    }
}
