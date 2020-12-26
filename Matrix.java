import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Matrix {
    /**
     * Object Matrix
     *
     * Created by 11811516 Wei Jinqi
     */
    final int rows;
    final int columns;
    private double[] Values;

    /**used for multithreading
     * Created by 11813223 Li Xingze
     */
    public static Matrix A1, A2, A3, A4, B1, B2, B3, B4, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, P1, P2, P3, P4, P5, P6, P7;


    /**
     * Created by 11811516 Wei Jinqi
     */
    /**
     * new a matrix 1*1
     */
    Matrix(){
        rows = 1;
        columns = 1;
        Values = new double[1];
    }
    /**
     * generate a row*col matrix
     * @param row number of rows
     * @param col number of columns
     */
    Matrix(int row,int col){
        rows = row;
        columns = col;
        Values = new double[row*col];
    }
    /**
     * generate a row*col matrix by using giving array
     * @param row number of rows
     * @param col number of columns
     * @param vals the elements array
     */
    Matrix(int row,int col,double[] vals){
        if(vals.length==row*col){
            rows = row;
            columns = col;
            Values = vals;
        }else{
            rows = row;
            columns = col;
            Values = new double[row*col];
        }
    }
    /**
     * Basic operations for Object Matrix
     *
     * Created by 11811516 Wei Jinqi
     */
    /**
     * get the value in matrix
     * @param row the nth row the element in
     * @param col the mth column the element in
     * @return the value
     */
    double getValueAt(int row,int col){
        if(row<1||col<1||row>rows||col>columns){
            return -1.404;
        }
        return Values[columns * (row-1) + col -1];
    }
    /**
     * *get a small matrix in a big matrix by the elements at the upper left corner and lower right corner
     *
     * @param r1 The number of rows of the upper left corner element of the matrix to be taken out.
     * @param c1 The number of columns of the upper left corner element of the matrix to be taken out.
     * @param r2 The number of rows of the lower right corner element of the matrix to be taken out.
     * @param c2 The number of columns of the lower right corner element of the matrix to be taken out.
     * @return A matrix within a rectangular interval specified by two elements.
     */
    Matrix getMatrixAt(int r1,int c1,int r2,int c2){
        if(r1>r2||c1>c2||r1<1||c1<1||r2>rows||c2>columns){
            return this;
        }
        Matrix ans = new Matrix(r2-r1+1,c2-c1+1);
        for(int i=0;i<ans.rows;++i){
            for(int j=0;j<ans.columns;++j){
                ans.Values[ans.columns * i + j] = Values[this.columns * (r1-1+i) + (c1-1+j)];
            }
        }
        return ans;
    }
    /**
     *
     * @return the Values array
     */
    double[] getValues(){
        if(Values.length < 1){
            return new double[1];
        }
        return Values;
    }
    /**
     * set the value in matrix
     * @param row the nth row the element you want to set in
     * @param col the mth column the element you want to set in
     * @param val the value of the element
     * @return true fot success and false for not
     */
    boolean setValueAt(int row,int col,double val){
        if(row<1||col<1||row>rows||col>columns){
            return false;
        }
        Values[columns * (row-1) + col -1] = val;
        return true;
    }
    /**
     *
     * @return a String to show the matrix like we see in Math
     */
    public String toString(){
        //String ans = "[";
        String ans = "";
        for(int i=1;i<Values.length+1;++i){
            if(i%columns == 0){
                ans += (Values[i-1] +"\n");
            }else{
                ans += (Values[i-1] + ",");
            }
        }
        //ans += "]";
        return ans;
    }
    /**
     * print the matrix(String)
     */
    void show(){
        System.out.println(this.toString());
    }

    /**
     * The 4 tasks w have to finish in DSAA(B)
     *
     * Created by 11811516 Wei Jinqi
     */

    public static void calibrate_crossover_point()throws IOException {
        Properties properties = new Properties();
        int cp = 2;
        long[] ls = {100,100,100,100,100,100,100,100,100,100};  //last 10 running time of strassen multiply
        long[] ln = {10,10,10,10,10,10,10,10,10,10};  //last running time of square multiply
        long startTime,endTime;
        for(int i=32;i<4098;++i){
            Matrix t1 = RandomMatrix(i,i);
            Matrix t2 = RandomMatrix(i,i);

            startTime = System.currentTimeMillis();
            strassen_multithreading(t1,t2);
            /**strassen_optimization_array(t1,t2);*/
            endTime = System.currentTimeMillis();
            ls[9] = endTime - startTime;
            //System.out.print(ls[9]+",");
            startTime = System.currentTimeMillis();
            square_matrix_multiply(t1,t2);
            endTime = System.currentTimeMillis();
            ln[9] = endTime - startTime;
            //System.out.println(ln[9]);
            /**If ten consecutive values ​​cross, the middle of them is the cross point */
            if(ln[0]<=ls[0] &&ln[1]<=ls[1] &&ln[2]<=ls[2] && ln[3]<=ls[3] && ln[4]<=ls[4] && ln[5]>=ls[5] && ln[6]>ls[6] && ln[7]>ls[7] && ln[8]>ls[8] && ln[9]>ls[9]){
                cp = i-4;
                properties.setProperty("CP", Long.toString(cp));
                properties.store(new FileWriter("crosspoint.properties"), "cross");
                return;
            }
            /** If the intersection is ignored, but for ten consecutive values, strassen is faster than ordinary multiplication, then the first value is set as the intersection */
            if(ln[0]>ls[0] &&ln[1]>ls[1] &&ln[2]>ls[2] && ln[3]>ls[3] && ln[4]>ls[4] && ln[5]>ls[5] && ln[6]>ls[6] && ln[7]>ls[7] && ln[8]>ls[8] && ln[9]>ls[9]){
                cp = i-9;
                properties.setProperty("CP", Long.toString(cp));
                properties.store(new FileWriter("crosspoint.properties"), "cross");
                return;
            }
            for(int j=0;j<ls.length-1;++j){
                ls[j] = ls[j+1];
                ln[j] = ln[j+1];
            }
        }
        /** if not found set -1 means none*/
        properties.setProperty("CP", Long.toString(-1));
        properties.store(new FileWriter("person.properties"), "cross");
    }
    /**
     * square_matrix_mutliply O(n^3)
     * @param a left matrix
     * @param b right matrix
     * @return result matrix
     */
    public static Matrix square_matrix_multiply(Matrix a, Matrix b){
        if(a.rows!=b.columns ){
            return new Matrix();
        }
        Matrix ans = new Matrix(a.rows,b.columns);
        for (int i=0;i<a.rows;++i){
            for(int j=0;j<b.columns;++j){
                ans.Values[ans.columns * i + j] = 0;
                for (int k=0;k<a.columns;++k){
                    ans.Values[ans.columns * i + j] += a.getValueAt(i+1,k+1)*b.getValueAt(k+1,j+1);
                }
            }
        }
        return ans;
    }
    /**
     * the task we have to finish
     * @param a matrix a
     * @param b matrix b
     * @return ans matrix
     */
    public static Matrix strassen_multiply(Matrix a, Matrix b){

        /**choose one from three ways*/
        //return strassen_matrix(a,b);
        //return strassen_optimization_array(a,b);
        return strassen_multithreading(a,b);
    }
    /** use normal(square_matrix_multiply) before crosspoint; use strassen (square_matrix_multiply) after crosspoint*/
    public static Matrix adaptive_multiply(Matrix a, Matrix b){
        int cp = 220;
        File file = new File("crosspoint.properties");
        if (file.exists()) {
            Properties properties = new Properties();
            try {
                properties.load(new FileReader("crosspoint.properties"));
            }catch (Exception e){}
            cp = Integer.parseInt(properties.getProperty("CP"));
        }

        if(a.rows>=cp){
            return strassen_multithreading(a,b);
            //return strassen_optimization_array(a,b);
        }else{
            return square_matrix_multiply(a,b);
        }

    }

    /**
     * used for test
     * @param n order of matrix
     *
     *          Created by 11811516 Wei Jinqi
     */
    public static void testmultiply(int n){
        System.out.print("n = " + n+" ,");
        /**Generate matrix*/
        long startTime = System.currentTimeMillis();
        Matrix t1 = RandomMatrix(n,n);
        Matrix t2 = RandomMatrix(n,n);
        long endTime = System.currentTimeMillis();
        //System.out.print("Generating Time： " + (endTime - startTime) + " ms，");

        /** square multiply*/
//        startTime = System.currentTimeMillis();
//        square_matrix_multiply(t1,t2);
//        endTime = System.currentTimeMillis();
//        System.out.println("Normal Running Time： " + (endTime - startTime) + " ms，");
        /**strassen Not optimized*/
//        startTime = System.currentTimeMillis();
//        strassen_multiply(t1,t2);
//        endTime = System.currentTimeMillis();
//        System.out.println("Strassen Running Time： " + (endTime - startTime) + " ms；");

        /**strassen optimization_array*/
//        startTime = System.currentTimeMillis();
//        strassen_optimization_array(t1,t2);
//        endTime = System.currentTimeMillis();
//        System.out.println("optimization Running Time： " + (endTime - startTime) + " ms；");
        /**strassen multithreading*/
        startTime = System.currentTimeMillis();
        strassen_multithreading(t1,t2);
        endTime = System.currentTimeMillis();
        System.out.println("multithreading Running Time： " + (endTime - startTime) + " ms；");
    }

    /**
     Generate rand matrix r*c
     the elements are between 1 and -1    [-1,1]     and two decimal places

     Created by 11811314 Luo Peiyu
     */
    /**
     *
     * @param r num of rows
     * @param c num of cols
     * @return Matrix r*c
     */
    public static Matrix RandomMatrix(int r,int c) {
        if(r > 0 && c > 0) {
        }else{
            Random ran = new Random();
            r = ran.nextInt()+1;
            c = ran.nextInt()+1;
        }
        double[] matrix = new double[r*c];
        for(int i = 0;i<r*c;i++){
            matrix[i] = RandomNum();
        }
        return new Matrix(r,c,matrix);
    }
    /**
     *
     * @return random number(double.00)
     */
    private static double RandomNum() {
        Random r = new Random();
        double number = 0;
        number = (double) (r.nextInt(201)) / 100 - 1;
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(number));
    }

    /**
     Strassen multiply which is using object Matrix for any n*n matrix.
     And these are the method have to use in this way.

     Created by 11811516 Wei Jinqi
     */
    /**
     *  strassen multiply by using object Matrix
     * @param a multiplier a
     * @param b multiplier b
     * @return ans Matrix
     */
    public static Matrix strassen_matrix(Matrix a, Matrix b){
        if(a.rows!=b.columns ){
            return new Matrix();
        }else if(a.rows<129){
            return square_matrix_multiply(a,b);
        }
        Matrix a11 = a.partitionMatrix(1);
        Matrix a12 = a.partitionMatrix(2);
        Matrix a21 = a.partitionMatrix(3);
        Matrix a22 = a.partitionMatrix(4);
        Matrix b11 = b.partitionMatrix(1);
        Matrix b12 = b.partitionMatrix(2);
        Matrix b21 = b.partitionMatrix(3);
        Matrix b22 = b.partitionMatrix(4);

        Matrix p1 = strassen_multiply(a11,minus(b12,b22));
        Matrix p2 = strassen_multiply(plus(a11,a12),b22);
        Matrix p3 = strassen_multiply(plus(a21,a22),b11);
        Matrix p4 = strassen_multiply(a22,minus(b21,b11));
        Matrix p5 = strassen_multiply(plus(a11,a22),plus(b11,b22));
        Matrix p6 = strassen_multiply(minus(a12,a22),plus(b21,b22));
        Matrix p7 = strassen_multiply(minus(a11,a21),plus(b11,b12));

        Matrix c11 = plus(minus(plus(p5,p4),p2),p6);
        Matrix c12 = plus(p1,p2);
        Matrix c21 = plus(p3,p4);
        Matrix c22 = minus(minus(plus(p5,p1),p3),p7);

        Matrix ans = mergeMatrix(c11,c12,c21,c22);
        return ans.getMatrixAt(1,1,a.rows,a.columns);
    }
    /**partition Matrix into 4 square matrix
     *
     * @param n 1 for LU, 2 for RU, 3 for LD, 4 for RD.
     * @return the matrix which is at position n.
     */
    public Matrix partitionMatrix(int n){
        if(rows!=columns|| rows<2 || columns<2){
            return this;
        }

        int r = rows/2;
        int c = columns/2;
        if(rows%2==0){
        }else{
            r+=1;c+=1;
        }
        double[] ans = new double[r*c];
        switch (n){
            case 1:
                for(int i=0;i<c;++i){
                    for(int j=0;j<r;++j){
                        ans[j*c+i]=Values[j*columns+i];
                    }
                }
                break;
            case 2:
                for(int i=0;i<(columns - c);++i){
                    for(int j=0;j<r;++j){
                        ans[j*c+i]=Values[j*columns+c+i];
                    }
                }
                break;
            case 3:
                for(int i=0;i<c;++i){
                    for(int j=0;j<(rows - r);++j){
                        ans[j*c+i]=Values[(j+r)*columns+i];
                    }
                }
                break;
            case 4:
                for(int i=0;i<(columns - c);++i){
                    for(int j=0;j<(rows - r);++j){
                        ans[j*c+i]=Values[(j+r)*columns+c+i];
                    }
                }
                break;
            default:
                break;

        }
        return new Matrix(r,c,ans);
    }
    /**
     *  combine the 4 matrix into a big one
     * @param A left up
     * @param B right up
     * @param C left down
     * @param D right down
     * @return thr merged matrix
     */
    public static Matrix mergeMatrix(Matrix A,Matrix B,Matrix C,Matrix D){
        if(A.rows!=A.columns ||B.rows!=B.columns || C.rows!=C.columns ||D.rows!=D.columns ||A.rows!=B.rows || B.rows!=C.rows||C.rows !=D.rows){
            return new Matrix();
        }
        int r =(A.rows+A.rows) ;
        int c = (A.columns+A.columns);
        double[] ans = new double[r*c];
        for(int j=0;j<r;++j){
            for(int i=0;i<c;++i){
                if(j<A.rows){
                    if(i<A.columns){
                        ans[j*c+i]=A.Values[j*A.columns+i];
                    }else{
                        ans[j*c+i]=B.Values[j*B.columns+i-B.columns];
                    }
                }else{
                    if(i<A.columns){
                        ans[j*c+i]=C.Values[(j-C.rows)*C.columns+i];
                    }else{
                        ans[j*c+i]=D.Values[(j-D.rows)*D.columns+i-D.columns];
                    }
                }
            }
        }
        return new Matrix(r,c,ans);
    }
    /**
     *
     * @param a left matrix
     * @param b right matrix
     * @return result matrix
     */
    public static Matrix plus(Matrix a,Matrix b){
        if(a.rows==b.columns && a.rows==b.rows){
            Matrix ans = new Matrix(a.rows,a.columns);
            for(int i=0;i<a.Values.length;++i){
                ans.Values[i] = a.Values[i] + b.Values[i];
            }
            return ans;
        }
        return new Matrix();
    }
    /**
     *
     * @param a left matrix
     * @param b right matrix
     * @return result matrix
     */
    public static Matrix minus(Matrix a,Matrix b){
        if(a.rows==b.columns && a.rows==b.rows){
            Matrix ans = new Matrix(a.rows,a.columns);
            for(int i=0;i<a.Values.length;++i){
                ans.Values[i] = a.Values[i] - b.Values[i];
            }
            return ans;
        }
        return new Matrix();
    }



    /**
       Strassen multiply which is optimized by function call method
       Do not use return value instead of parameter to reduce the time generate a return object.
       This operation is similar to use pointer .  Although Java do not have pointer, it passes parameter which is array by passing the array's 'pointer' or 'address'.
       And these are the method have to use in this way

        Created by 11811516 Wei Jinqi
     */
    /**
     *
     * @param a multiplier a
     * @param b multiplier b
     * @return ans matrix
     */
    public static Matrix strassen_optimization_array(Matrix a, Matrix b) {
        double[] ans = new double[a.rows*a.rows];
        strassen_array(ans,a.Values,b.Values,a.rows);
        return new Matrix(a.rows,a.rows,ans);
    }
    /**
     * strassen multiply by using array directly   only for n*n matrix
     * @param ans ans array
     * @param a multiplier a
     * @param b multiplier b
     * @param n the order of square matrix a/b
     */
    public static void strassen_array(double[] ans,double[] a,double[] b,int n){
        if(n<3){
            square(ans,a,b,n);
            return;
        }
        int m = n/2;
        if(n%2==0){
        }else{
            m+=1;
        }

        int mm = m*m;
        double[] a11 = new double[mm];
        double[] a12 = new double[mm];
        double[] a21 = new double[mm];
        double[] a22 = new double[mm];
        parti(a,n,m,a11,a12,a21,a22);
        double[] b11 = new double[mm];
        double[] b12 = new double[mm];
        double[] b21 = new double[mm];
        double[] b22 = new double[mm];
        parti(b,n,m,b11,b12,b21,b22);

        double[] s1=new double[mm],s2=new double[mm],s3=new double[mm],s4=new double[mm],s5=new double[mm],s6=new double[mm],s7=new double[mm],s8=new double[mm],s9=new double[mm],s10 =new double[mm];

        minusp(s1,b12,b22);
        plusp(s2,a11,a12);
        minusp(s4,b21,b11);
        plusp(s3,a21,a22);
        plusp(s5,a11,a22);
        plusp(s6,b11,b22);
        minusp(s7,a12,a22);
        plusp(s8,b21,b22);
        minusp(s9,a11,a21);
        plusp(s10,b11,b12);

        double[] p1=new double[mm],p2=new double[mm],p3=new double[mm],p4=new double[mm],p5=new double[mm],p6=new double[mm],p7=new double[mm];
        strassen_array(p1,a11,s1,m);
        strassen_array(p2,s2,b22,m);
        strassen_array(p3,s3,b11,m);
        strassen_array(p4,a22,s4,m);
        strassen_array(p5,s5,s6,m);
        strassen_array(p6,s7,s8,m);
        strassen_array(p7,s9,s10,m);
        double[] c11 =new double[mm],c12 =new double[mm],c21 =new double[mm],c22 =new double[mm];
        minusspec(c11,p5,p4,p2,p6);
        plusp(c12,p1,p2);
        plusp(c21,p3,p4);
        plusspec(c22,p5,p1,p3,p7);
        merge(ans,n,m,c11,c12,c21,c22);

    }
    /**
     *  square multiply (array)
     * @param ans ans array
     * @param a multiplier a
     * @param b multiplier b
     * @param n a/b/ans's order n.
     */
    public static void square(double[] ans,double[] a,double[] b,int n){
        for (int i=0;i<n;++i){
            for(int j=0;j<n;++j){
                ans[n * i + j] = 0;
                for (int k=0;k<n;++k){
                    ans[n * i + j] += a[n*i+k]*b[n*k+j];
//                    ans.Values[ans.columns * i + j] += a.getValueAt(i+1,k+1)*b.getValueAt(k+1,j+1);
//                    return Values[columns * (row-1) + col -1];
                }
            }
        }
    }
    /**
     * partition matrix into 4 parts
     * @param a the matrix have to be partitioned
     * @param n a's order
     * @param m a11' order      (m is not always equals n/2 because n is not always even)
     * @param a11 LU
     * @param a12 RU
     * @param a21 LD
     * @param a22 RD
     */
    static void parti(double[] a,int n,int m,double[] a11,double[] a12,double[] a21,double[] a22){
        for(int i=0;i<m;++i){
            for(int j=0;j<m;++j){
                a11[j*m+i]=a[j*n+i];
                if(i<(n - m)){a12[j*m+i]=a[j*n+m+i];}
                if(j<(n - m)){a21[j*m+i]=a[(j+m)*n+i];}
                if(i<(n - m)&&j<(n - m)){a22[j*m+i]=a[(j+m)*n+m+i];}
            }
        }
    }
    /**
     *
     * @param a ans merged the 4 matrix
     * @param n a's order
     * @param m a11's order
     * @param a11 LU
     * @param a12 RU
     * @param a21 LD
     * @param a22 RD
     */
    static void merge(double[] a,int n,int m,double[] a11,double[] a12,double[] a21,double[] a22){
        for(int j=0;j<n;++j){
            for(int i=0;i<n;++i){
                if(j<m){
                    if(i<m){
                        a[j*n+i]=a11[j*m+i];
                    }else{
                        a[j*n+i]=a12[j*m+i-m];
                    }
                }else{
                    if(i<m){
                        a[j*n+i]=a21[(j-m)*m+i];
                    }else{
                        a[j*n+i]=a22[(j-m)*m+i-m];
                    }
                }
            }
        }
    }
    /**
     *
     * @param ans ans=a+b
     * @param a a+b 's a
     * @param b a+b 's b
     */
    static void plusp(double[] ans,double[] a,double[] b){
        for(int i=0;i<ans.length;++i){
            ans[i] = a[i] + b[i];
        }
    }
    /**
     *
     * @param ans ans=a-b
     * @param a a-b 's a
     * @param b a-b 's b
     */
    static void minusp(double[] ans,double[] a,double[] b){
        for(int i=0;i<ans.length;++i){
            ans[i] = a[i] - b[i];
        }
    }
    /**
     *special plus operation to calculate c11
     * @param ans ans
     * elements in series
     * @param a5
     * @param a4
     * @param a2
     * @param a6
     */
    static void minusspec(double[] ans,double[] a5,double[] a4,double[] a2,double[] a6){
        for(int i=0;i<ans.length;++i){
            ans[i] = a5[i] + a4[i] - a2[i] + a6[i];
        }
    }
    /**
     * special minus operation to calculate c22
     * @param ans ans
     *            elements in series
     * @param a5
     * @param a1
     * @param a3
     * @param a7
     */
    static void plusspec(double[] ans,double[] a5,double[] a1,double[] a3,double[] a7){
        for(int i=0;i<ans.length;++i){
            ans[i] = a5[i] + a1[i] - a3[i] -a7[i];
        }
    }

    /**strasen multiply by using Object Matrix  and  multithreading
     *
     * Created by 11813223 Li Xingze
     */
    public static Matrix strassen_multithreading(Matrix a, Matrix b){
        if(a.rows!=b.columns ){
            return new Matrix();
        }else if(a.rows<128){
            return square_matrix_multiply(a,b);
        }
        Matrix a11 = a.partitionMatrix(1);
        Matrix a12 = a.partitionMatrix(2);
        Matrix a21 = a.partitionMatrix(3);
        Matrix a22 = a.partitionMatrix(4);
        Matrix b11 = b.partitionMatrix(1);
        Matrix b12 = b.partitionMatrix(2);
        Matrix b21 = b.partitionMatrix(3);
        Matrix b22 = b.partitionMatrix(4);


        ThreadPoolExecutor executor1 = new ThreadPoolExecutor(10, 15, 200,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
        MyTask myTask1 = new MyTask("threadMinus","S1", b12, b22);
        MyTask myTask2 = new MyTask("threadAdd","S2", a11, a12);
        MyTask myTask3 = new MyTask("threadAdd","S3", a21, a22);
        MyTask myTask4 = new MyTask("threadMinus","S4", b21, b11);
        MyTask myTask5 = new MyTask("threadAdd","S5", a11, a22);
        MyTask myTask6 = new MyTask("threadAdd","S6", b11, b22);
        MyTask myTask7 = new MyTask("threadMinus","S7", a12, a22);
        MyTask myTask8 = new MyTask("threadAdd","S8", b21, b22);
        MyTask myTask9 = new MyTask("threadMinus","S9", a11, a21);
        MyTask myTask10 = new MyTask("threadAdd","S10", b11, b12);
        executor1.execute(myTask1);
        executor1.execute(myTask2);
        executor1.execute(myTask3);
        executor1.execute(myTask4);
        executor1.execute(myTask5);
        executor1.execute(myTask6);
        executor1.execute(myTask7);
        executor1.execute(myTask8);
        executor1.execute(myTask9);
        executor1.execute(myTask10);
        while (true) {
            if (executor1.getPoolSize() == executor1.getCompletedTaskCount()) {
                executor1.shutdown();
                break;
            }
        }
        ThreadPoolExecutor executor2 = new ThreadPoolExecutor(10, 15, 200,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
        MyTask myTask11 = new MyTask("thread_matrix_product","P1", a11, S1);
        MyTask myTask12 = new MyTask("thread_matrix_product","P2", S2, b22);
        MyTask myTask13 = new MyTask("thread_matrix_product","P3", S3, b11);
        MyTask myTask14 = new MyTask("thread_matrix_product","P4", a22, S4);
        MyTask myTask15 = new MyTask("thread_matrix_product","P5", S5, S6);
        MyTask myTask16 = new MyTask("thread_matrix_product","P6", S7, S8);
        MyTask myTask17 = new MyTask("thread_matrix_product","P7", S9, S10);
        executor2.execute(myTask11);
        executor2.execute(myTask12);
        executor2.execute(myTask13);
        executor2.execute(myTask14);
        executor2.execute(myTask15);
        executor2.execute(myTask16);
        executor2.execute(myTask17);
        while (true) {
            if (executor2.getPoolSize() == executor2.getCompletedTaskCount()) {
                executor2.shutdown();
                break;
            }
        }
        ThreadPoolExecutor executor3 = new ThreadPoolExecutor(10, 15, 200,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
        MyTask myTask18 = new MyTask("threadAdd","S5", P5, P4);
        MyTask myTask19 = new MyTask("threadMinus","S6", P2, P6);
        MyTask myTask20 = new MyTask("threadAdd","S7", P5, P1);
        MyTask myTask21 = new MyTask("threadAdd","S8", P3, P7);
        executor3.execute(myTask18);
        executor3.execute(myTask19);
        executor3.execute(myTask20);
        executor3.execute(myTask21);
        while (true) {
            if (executor3.getPoolSize() == executor3.getCompletedTaskCount()) {
                executor3.shutdown();
                break;
            }
        }
        ThreadPoolExecutor executor4 = new ThreadPoolExecutor(10, 15, 200,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
        MyTask myTask22 = new MyTask("threadMinus","S1", S5, S6);
        MyTask myTask23 = new MyTask("threadAdd","S2", P1, P2);
        MyTask myTask24 = new MyTask("threadAdd","S3", P3, P4);
        MyTask myTask25 = new MyTask("threadMinus","S4", S7, S8);
        executor4.execute(myTask22);
        executor4.execute(myTask23);
        executor4.execute(myTask24);
        executor4.execute(myTask25);
        while (true) {
            if (executor4.getPoolSize() == executor4.getCompletedTaskCount()) {
                executor4.shutdown();
                break;
            }
        }
        Matrix ans = mergeMatrix(S1,S2,S3,S4);
        return ans.getMatrixAt(1,1,a.rows,a.columns);
    }
    static class MyTask implements Runnable{
        private String method;
        private String ans;
        private Matrix matrixA;
        private Matrix matrixB;

        public MyTask(String method, String ans, Matrix matrixA, Matrix matrixB) {
            this.method = method;
            this.ans = ans;
            this.matrixA = matrixA;
            this.matrixB = matrixB;
        }

        @Override
        public void run() {
            Matrix matrix = new Matrix(matrixA.rows,matrixB.columns);
            if (method.equals("threadAdd")) {
                matrix = plus(matrixA, matrixB);
            } else if (method.equals("threadMinus")) {
                matrix = minus(matrixA, matrixB);
            } else if (method.equals("thread_matrix_product")) {
                matrix = square_matrix_multiply(matrixA, matrixB);
            }
            Applyer.applyTo(ans, matrix);
        }
    }

}

/**used for multithreading
 *
 * Created by 11813223 Li Xingze
 */
class Applyer{
    public static Matrix temp;
    public static synchronized void applyTo(String ans, Matrix matrix) {
        if (ans.equals("S1")) {
            temp.S1 = matrix;
        } else if (ans.equals("S2")) {
            temp.S2 = matrix;
        } else if (ans.equals("S3")) {
            temp. S3 = matrix;
        } else if (ans.equals("S4")) {
            temp.S4 = matrix;
        } else if (ans.equals("S5")) {
            temp.S5 = matrix;
        } else if (ans.equals("S6")) {
            temp.S6 = matrix;
        } else if (ans.equals("S7")) {
            temp.S7 = matrix;
        } else if (ans.equals("S8")) {
            temp.S8 = matrix;
        } else if (ans.equals("S9")) {
            temp.S9 = matrix;
        } else if (ans.equals("S10")) {
            temp.S10 = matrix;
        } else if (ans.equals("A1")) {
            temp.A1 = matrix;
        } else if (ans.equals("A2")) {
            temp.A2 = matrix;
        } else if (ans.equals("A3")) {
            temp.A3 = matrix;
        } else if (ans.equals("A4")) {
            temp.A4 = matrix;
        } else if (ans.equals("B1")) {
            temp.B1 = matrix;
        } else if (ans.equals("B2")) {
            temp.B2 = matrix;
        } else if (ans.equals("B3")) {
            temp.B3 = matrix;
        } else if (ans.equals("B4")) {
            temp.B4 = matrix;
        } else if (ans.equals("P1")) {
            temp.P1 = matrix;
        } else if (ans.equals("P2")) {
            temp.P2 = matrix;
        } else if (ans.equals("P3")) {
            temp.P3 = matrix;
        } else if (ans.equals("P4")) {
            temp.P4 = matrix;
        } else if (ans.equals("P5")) {
            temp.P5 = matrix;
        } else if (ans.equals("P6")) {
            temp.P6 = matrix;
        } else if (ans.equals("P7")) {
            temp.P7 = matrix;
        }
    }
}