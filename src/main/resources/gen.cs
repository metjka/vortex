using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.Globalization;
using System.IO;
using System.Reflection;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text.RegularExpressions;
using cnt_n;


namespace CntProceduralTextures
{
    public class Gen
    {
        // Decompiled with JetBrains decompiler
// Type: cnt_n.Gen
// Assembly: Gen, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null
// MVID: 9C9BEE5B-A1B3-41E6-9558-5F0DCF7D1EB9
// Assembly location: C:\Users\isalnikov\Desktop\CntProceduralTextures\CntProceduralTextures\bin\Debug\Gen.dll


        private DateTime timer = new DateTime();
        public Dictionary<string, Bitmap> Bitmaps = new Dictionary<string, Bitmap>();
        private bool seamlessX = true;
        private bool seamlessY = true;
        private bool seamlessZ = true;
        public cnt_f1D ff1d = (cnt_f1D) (x1 => 1f - x1 * x1);
        public cnt_f ff = (cnt_f) ((x1, y1) => 1f - (float) ((double) x1 * (double) x1 + (double) y1 * (double) y1));

        public cnt_f3D ff3d =
            (cnt_f3D)
            ((x1, y1, z1) =>
                    1f - (float) ((double) x1 * (double) x1 + (double) y1 * (double) y1 + (double) z1 * (double) z1));

        private float cloudDensity = 1f;
        private bool fastPerlin = true;
        private List<cnt_f1D> fPipe = new List<cnt_f1D>();
        private List<Color> ColorsGradient = new List<Color>();
        private List<Color> ColorsGradientLat = new List<Color>();
        private float Lat01Factor = 0.7f;
        private static float[] grad = new float[256];

        private static int[] p = new int[256]
        {
            151,
            160,
            137,
            91,
            90,
            15,
            131,
            13,
            201,
            95,
            96,
            53,
            194,
            233,
            7,
            225,
            140,
            36,
            103,
            30,
            69,
            142,
            8,
            99,
            37,
            240,
            21,
            10,
            23,
            190,
            6,
            148,
            247,
            120,
            234,
            75,
            0,
            26,
            197,
            62,
            94,
            252,
            219,
            203,
            117,
            35,
            11,
            32,
            57,
            177,
            33,
            88,
            237,
            149,
            56,
            87,
            174,
            20,
            125,
            136,
            171,
            168,
            68,
            175,
            74,
            165,
            71,
            134,
            139,
            48,
            27,
            166,
            77,
            146,
            158,
            231,
            83,
            111,
            229,
            122,
            60,
            211,
            133,
            230,
            220,
            105,
            92,
            41,
            55,
            46,
            245,
            40,
            244,
            102,
            143,
            54,
            65,
            25,
            63,
            161,
            1,
            216,
            80,
            73,
            209,
            76,
            132,
            187,
            208,
            89,
            18,
            169,
            200,
            196,
            135,
            130,
            116,
            188,
            159,
            86,
            164,
            100,
            109,
            198,
            173,
            186,
            3,
            64,
            52,
            217,
            226,
            250,
            124,
            123,
            5,
            202,
            38,
            147,
            118,
            126,
            (int) byte.MaxValue,
            82,
            85,
            212,
            207,
            206,
            59,
            227,
            47,
            16,
            58,
            17,
            182,
            189,
            28,
            42,
            223,
            183,
            170,
            213,
            119,
            248,
            152,
            2,
            44,
            154,
            163,
            70,
            221,
            153,
            101,
            155,
            167,
            43,
            172,
            9,
            129,
            22,
            39,
            253,
            19,
            98,
            108,
            110,
            79,
            113,
            224,
            232,
            178,
            185,
            112,
            104,
            218,
            246,
            97,
            228,
            251,
            34,
            242,
            193,
            238,
            210,
            144,
            12,
            191,
            179,
            162,
            241,
            81,
            51,
            145,
            235,
            249,
            14,
            239,
            107,
            49,
            192,
            214,
            31,
            181,
            199,
            106,
            157,
            184,
            84,
            204,
            176,
            115,
            121,
            50,
            45,
            (int) sbyte.MaxValue,
            4,
            150,
            254,
            138,
            236,
            205,
            93,
            222,
            114,
            67,
            29,
            24,
            72,
            243,
            141,
            128,
            195,
            78,
            66,
            215,
            61,
            156,
            180
        };

        public Random r;
        private int n_dimensions;
        private int wid;
        private int hei;
        private int zlvl;
        public int Seed;
        private int startMarginX;
        private int endMarginX;
        private int startMarginY;
        private int endMarginY;
        private int startMarginZ;
        private int endMarginZ;
        public int[,] ptsDistr;
        public bool storeBitmaps;
        private string log;
        public bool error;
        private float[] _Values1D;
        private CachedStream CS;
        public bool useFS;
        public int FSblockSize;
        public int FSstackNr;
        private Gen.flatCoord fc;
        public bool isCB;
        public string Script;
        private float cloudCoverage;
        private bool useFuncPipe;

        static Gen()
        {
            Gen.InitPerlinPerm((uint) new Random().Next());
        }

        public Gen(int w, int seed)
        {
            if (w > 0)
                this.set1D(w);
            this.init(seed);
        }

        public Gen(int w, int h, int seed)
        {
            if (w > 0 && h > 0)
                this.set2D(w, h);
            this.init(seed);
        }

        public Gen(int w, int h, int zl, int seed)
        {
            if (w > 0 && h > 0 && zl > 0)
                this.set3D(w, h, zl);
            this.init(seed);
        }

        public void erasePtsDistr()
        {
            this.ptsDistr = (int[,]) null;
        }

        public void createPtsDistr(int nX, int nY, int rndX, int rndY, int lineShift, int colShift)
        {
            this.ptsDistr = new int[nX * nY, 3];
            int num1 = (this.wid - this.startMarginX - this.endMarginX) / nX;
            int num2 = (this.hei - this.startMarginY - this.endMarginY) / nY;
            int index1 = 0;
            for (int index2 = 0; index2 < nX; ++index2)
            {
                for (int index3 = 0; index3 < nY; ++index3)
                {
                    int i1 = this.startMarginX + index2 * num1 + lineShift * index3;
                    int i2 = this.startMarginY + index3 * num2 + colShift * index2;
                    if (rndX > 0)
                        i1 += this.r.Next(0, rndX);
                    if (rndX < 0)
                    {
                        int maxValue = -1 * rndX;
                        i1 += this.r.Next(rndX, maxValue);
                    }
                    if (rndY > 0)
                        i2 += this.r.Next(0, rndY);
                    if (rndY < 0)
                    {
                        int maxValue = -1 * rndY;
                        i2 += this.r.Next(rndY, maxValue);
                    }
                    int num3 = this.seamlessCoord(i1, this.wid, this.seamlessX);
                    int num4 = this.seamlessCoord(i2, this.hei, this.seamlessY);
                    this.ptsDistr[index1, 0] = num3;
                    this.ptsDistr[index1, 1] = num4;
                    this.ptsDistr[index1, 2] = this.r.Next(0, (int) byte.MaxValue);
                    ++index1;
                }
            }
        }

        public void createPtsDistr3D(int nX, int nY, int nZ, int rndX, int rndY, int rndZ, int lineShift, int colShift,
            int zShift)
        {
            this.ptsDistr = new int[nX * nY * nZ, 4];
            int num1 = (this.wid - this.startMarginX - this.endMarginX) / nX;
            int num2 = (this.hei - this.startMarginY - this.endMarginY) / nY;
            int num3 = (this.zlvl - this.startMarginZ - this.endMarginZ) / nZ;
            int index1 = 0;
            for (int index2 = 0; index2 < nX; ++index2)
            {
                for (int index3 = 0; index3 < nY; ++index3)
                {
                    for (int index4 = 0; index4 < nZ; ++index4)
                    {
                        int i1 = this.startMarginX + index2 * num1 + lineShift * index3;
                        int i2 = this.startMarginY + index3 * num2 + colShift * index2;
                        int i3 = this.startMarginZ + index4 * num3 + zShift * index4;
                        if (rndX > 0)
                            i1 += this.r.Next(0, rndX);
                        if (rndX < 0)
                        {
                            int maxValue = -1 * rndX;
                            i1 += this.r.Next(rndX, maxValue);
                        }
                        if (rndY > 0)
                            i2 += this.r.Next(0, rndY);
                        if (rndY < 0)
                        {
                            int maxValue = -1 * rndY;
                            i2 += this.r.Next(rndY, maxValue);
                        }
                        if (rndZ > 0)
                            i3 += this.r.Next(0, rndZ);
                        if (rndZ < 0)
                        {
                            int maxValue = -1 * rndZ;
                            i3 += this.r.Next(rndZ, maxValue);
                        }
                        int num4 = this.seamlessCoord(i1, this.wid, this.seamlessX);
                        int num5 = this.seamlessCoord(i2, this.hei, this.seamlessY);
                        int num6 = this.seamlessCoord(i3, this.zlvl, this.seamlessZ);
                        this.ptsDistr[index1, 0] = num4;
                        this.ptsDistr[index1, 1] = num5;
                        this.ptsDistr[index1, 2] = num6;
                        this.ptsDistr[index1, 3] = this.r.Next(0, (int) byte.MaxValue);
                        ++index1;
                    }
                }
            }
        }

        public void useFileStream(int blockSize, int stackNr)
        {
            this.useFS = true;
            this.FSblockSize = blockSize;
            this.FSstackNr = stackNr;
            this.initV((long) this.wid * (long) this.hei);
        }

        public string getCSReads()
        {
            if (this.useFS)
                return this.CS.readNr.ToString();
            return "";
        }

        public string getCSWrites()
        {
            if (this.useFS)
                return this.CS.writeNr.ToString();
            return "";
        }

        private float getV(long i)
        {
            return this._Values1D[i];
        }

        private float getV(int i, int j)
        {
            if (this.useFS)
                return this.CS.getV(i, j);
            return this.getV((long) (i + this.wid * j));
        }

        public int getZlvl()
        {
            return this.zlvl;
        }

        public int getWid()
        {
            return this.wid;
        }

        public int getHei()
        {
            return this.hei;
        }

        private int flat_coord_3d(int i, int j, int k)
        {
            return i + this.wid * (j + this.hei * k);
        }

        private int flat_coord_cb(int i, int j, int k)
        {
            if (k == 0)
                return this.flat_coord_3d(i, j, 0);
            if (k == this.zlvl - 1)
                return this.flat_coord_3d(i, j, 1);
            if (j == 0)
                return this.flat_coord_3d(i, k, 2);
            if (j == this.hei - 1)
                return this.flat_coord_3d(i, k, 3);
            if (i == 0)
                return this.flat_coord_3d(k, j, 4);
            if (i == this.wid - 1)
                return this.flat_coord_3d(k, j, 5);
            return 0;
        }

        private float getV(int i, int j, int k)
        {
            if (!this.useFS)
                return this.getV((long) this.fc(i, j, k));
            if (!this.isCB)
                return this.CS.getV(i, j, k);
            if (k == 0)
                return this.CS.getV(i, j, 0);
            if (k == this.zlvl - 1)
                return this.CS.getV(i, j, 1);
            if (j == 0)
                return this.CS.getV(i, k, 2);
            if (j == this.hei - 1)
                return this.CS.getV(i, k, 3);
            if (i == 0)
                return this.CS.getV(k, j, 4);
            if (i == this.wid - 1)
                return this.CS.getV(k, j, 5);
            return 0.0f;
        }

        private void setV(long i, float v)
        {
            this._Values1D[i] = v;
        }

        private void setV(int i, int j, float v)
        {
            if (this.useFS)
                this.CS.setV(i, j, v);
            else
                this.setV((long) (i + this.wid * j), v);
        }

        private void setV(int i, int j, int k, float v)
        {
            if (this.useFS)
            {
                if (this.isCB)
                {
                    if (k == 0)
                        this.CS.setV(i, j, 0, v);
                    if (k == this.zlvl - 1)
                        this.CS.setV(i, j, 1, v);
                    if (j == 0)
                        this.CS.setV(i, k, 2, v);
                    if (j == this.hei - 1)
                        this.CS.setV(i, k, 3, v);
                    if (i == 0)
                        this.CS.setV(k, j, 4, v);
                    if (i != this.wid - 1)
                        return;
                    this.CS.setV(k, j, 5, v);
                }
                else
                    this.CS.setV(i, j, k, v);
            }
            else
                this.setV((long) this.fc(i, j, k), v);
        }

        private void addV(int i, float v)
        {
            float v1 = this.getV((long) i);
            this.setV((long) i, v1 + v);
        }

        private void addV(int i, int j, float v)
        {
            if (this.useFS)
                this.CS.addV(i, j, v);
            else
                this.addV(i + this.wid * j, v);
        }

        private void addV(int i, int j, int k, float v)
        {
            if (this.useFS)
            {
                if (this.isCB)
                {
                    if (k == 0)
                        this.CS.addV(i, j, 0, v);
                    if (k == this.zlvl - 1)
                        this.CS.addV(i, j, 1, v);
                    if (j == 0)
                        this.CS.addV(i, k, 2, v);
                    if (j == this.hei - 1)
                        this.CS.addV(i, k, 3, v);
                    if (i == 0)
                        this.CS.addV(k, j, 4, v);
                    if (i != this.wid - 1)
                        return;
                    this.CS.addV(k, j, 5, v);
                }
                else
                    this.CS.addV(i, j, k, v);
            }
            else
                this.addV(this.fc(i, j, k), v);
        }

        private void initV(long dim)
        {
            if (this.useFS)
            {
                this.CS = new CachedStream("tmp", (long) this.wid, (long) this.hei, (long) this.zlvl, 4,
                    this.FSblockSize, this.FSstackNr);
            }
            else
            {
                try
                {
                    this._Values1D = new float[dim];
                }
                catch (OutOfMemoryException ex)
                {
                    this.error = true;
                    this.LogMsg(ref this.log, " * OUT OF MEMORY**, try lesser dimension values or use useFileStream !!");
                }
            }
        }

        private double nextGaussian(double mean, double variance)
        {
            double num = Math.Sqrt(-2.0 * Math.Log(this.r.NextDouble())) * Math.Sin(2.0 * Math.PI * this.r.NextDouble());
            return mean + variance * num;
        }

        private void init(int seed)
        {
            if (seed != 0)
            {
                this.Seed = seed;
                this.r = new Random(seed);
            }
            else
            {
                this.Seed = new Random().Next(0, 32000);
                this.r = new Random(this.Seed);
            }
            Gen.InitPerlinPerm((uint) seed);
        }

        private int sealDim(int v)
        {
            if (v > 512)
                return 512;
            return v;
        }

        public void set1D(int xDim)
        {
            xDim = this.sealDim(xDim);
            this.n_dimensions = 1;
            this.wid = xDim;
            this.initV((long) this.wid);
        }

        public void set2D(int xDim, int yDim)
        {
            xDim = this.sealDim(xDim);
            yDim = this.sealDim(yDim);
            this.n_dimensions = 2;
            this.wid = xDim;
            this.hei = yDim;
            this.initV((long) this.wid * (long) this.hei);
        }

        public void set3D(int xDim, int yDim, int zDim)
        {
            xDim = this.sealDim(xDim);
            yDim = this.sealDim(yDim);
            zDim = this.sealDim(zDim);
            this.n_dimensions = 3;
            this.wid = xDim;
            this.hei = yDim;
            this.zlvl = zDim;
            this.initV((long) this.wid * (long) this.hei * (long) this.zlvl);
            this.fc = new Gen.flatCoord(this.flat_coord_3d);
        }

        public void setCB(int cubedim)
        {
            cubedim = this.sealDim(cubedim);
            this.n_dimensions = 3;
            this.wid = cubedim;
            this.hei = cubedim;
            this.zlvl = cubedim;
            this.initV((long) this.wid * (long) this.wid * 6L);
            this.fc = new Gen.flatCoord(this.flat_coord_cb);
            this.isCB = true;
        }

        public void PerlinCloudCoverage(float f)
        {
            this.cloudCoverage = this.clamp(f, 0.0f, 1f);
        }

        public void PerlinCloudDendity(float f)
        {
            this.cloudDensity = this.clamp(f, 0.0f, 1f);
        }

        public void FastPerlin()
        {
            this.fastPerlin = true;
        }

        public void SlowPerlin()
        {
            this.fastPerlin = false;
        }

        private static void InitPerlinPerm(uint seed)
        {
            Random random = new Random((int) seed);
            for (uint index = 0; (long) index < (long) Gen.grad.GetLength(0); ++index)
                Gen.grad[(int) index] = (float) random.Next(0, 32000) / 32000f;
        }

        public void initPerlin(int seed)
        {
            if (seed == 0)
                seed = this.r.Next();
            Gen.InitPerlinPerm((uint) seed);
        }

        private float pseudoRandomFromPerm(int x, int y)
        {
            return Gen.grad[(x + Gen.p[y & (int) byte.MaxValue]) % Gen.grad.GetLength(0)];
        }

        private float noise(float x, float y, float w, float h)
        {
            if (this.seamlessX)
            {
                if ((double) x >= (double) w)
                    x %= w;
                if ((double) x < 0.0)
                    x = w + x;
            }
            if (this.seamlessY)
            {
                if ((double) y >= (double) h)
                    y %= h;
                if ((double) y < 0.0)
                    y = h + y;
            }
            return this.pseudoRandomFromPerm((int) x, (int) y);
        }

        private float SmoothNoise_1(float x, float y, float w, float h)
        {
            return
                (float)
                (((double) this.noise(x - 1f, y - 1f, w, h) + (double) this.noise(x + 1f, y - 1f, w, h) +
                  (double) this.noise(x - 1f, y + 1f, w, h) + (double) this.noise(x + 1f, y + 1f, w, h)) / 16.0) +
                (float)
                (((double) this.noise(x - 1f, y, w, h) + (double) this.noise(x + 1f, y, w, h) +
                  (double) this.noise(x, y - 1f, w, h) + (double) this.noise(x, y + 1f, w, h)) / 8.0) +
                this.noise(x, y, w, h) / 4f;
        }

        private float cos_Interpolate(float a, float b, float x)
        {
            float num = (float) ((1.0 - Math.Cos((double) (x * 3.141593f))) * 0.5);
            return a - num * (a - b);
        }

        private float lin_Interpolate(float a, float b, float x)
        {
            return a - x * (a - b);
        }

        private float InterpolatedNoise_1(float x, float y, float w, float h)
        {
            int num1 = (int) x;
            float x1 = x - (float) num1;
            int num2 = (int) y;
            float x2 = y - (float) num2;
            int num3 = num1 + 1;
            int num4 = num2 + 1;
            float a1 = this.SmoothNoise_1((float) num1, (float) num2, w, h);
            float b1 = this.SmoothNoise_1((float) num3, (float) num2, w, h);
            float a2 = this.SmoothNoise_1((float) num1, (float) num4, w, h);
            float b2 = this.SmoothNoise_1((float) num3, (float) num4, w, h);
            return this.cos_Interpolate(this.cos_Interpolate(a1, b1, x1), this.cos_Interpolate(a2, b2, x1), x2);
        }

        private float fast_InterpolatedNoise_1(float x, float y, float w, float h)
        {
            int num1 = (int) x;
            float x1 = x - (float) num1;
            int num2 = (int) y;
            float x2 = y - (float) num2;
            int num3 = num1 + 1;
            int num4 = num2 + 1;
            float a1 = this.noise((float) num1, (float) num2, w, h);
            float b1 = this.noise((float) num3, (float) num2, w, h);
            float a2 = this.noise((float) num1, (float) num4, w, h);
            float b2 = this.noise((float) num3, (float) num4, w, h);
            return this.lin_Interpolate(this.lin_Interpolate(a1, b1, x1), this.lin_Interpolate(a2, b2, x1), x2);
        }

        private double PerlinNoise2d(int x, int y, int W, int H, float x_frequency, float y_frequency, float persistence,
            float octaves, float amplitude)
        {
            double num1 = 0.0;
            for (int index = 0; (double) index < (double) octaves; ++index)
            {
                if (this.fastPerlin)
                    num1 +=
                        (double)
                        this.fast_InterpolatedNoise_1((float) x * x_frequency, (float) y * y_frequency,
                            (float) W * x_frequency, (float) H * y_frequency) * (double) amplitude;
                else
                    num1 +=
                        (double)
                        this.InterpolatedNoise_1((float) x * x_frequency, (float) y * y_frequency,
                            (float) W * x_frequency, (float) H * y_frequency) * (double) amplitude;
                if (num1 <= 1.0)
                {
                    x_frequency *= 2f;
                    y_frequency *= 2f;
                    amplitude *= persistence;
                }
                else
                    break;
            }
            double num2 = (num1 - (double) this.cloudCoverage) * (double) this.cloudDensity;
            if (num2 < 0.0)
                num2 = 0.0;
            if (num2 > 1.0)
                num2 = 1.0;
            return num2;
        }

        public void PerlinNoise(float x_frequency, float y_frequency, float persistence, float octaves, float amplitude)
        {
            x_frequency = 1f / x_frequency;
            y_frequency = 1f / y_frequency;
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < this.hei; ++index2)
                {
                    double num = this.PerlinNoise2d(index1, index2, this.wid, this.hei, x_frequency, y_frequency,
                        persistence, octaves, amplitude);
                    this.setV(index1, index2, (float) (num * (double) byte.MaxValue));
                }
            }
        }

        private float pseudoRandomFromPerm(int x, int y, int z)
        {
            return
                Gen.grad[
                    (x + Gen.p[y + Gen.p[z & (int) byte.MaxValue] & (int) byte.MaxValue]) %
                    Gen.grad.GetLength(0)];
        }

        private float noise(float x, float y, float z, float w, float h, float zl)
        {
            if (this.seamlessX)
            {
                if ((double) x >= (double) w)
                    x %= w;
                if ((double) x < 0.0)
                    x = w + x;
            }
            if (this.seamlessY)
            {
                if ((double) y >= (double) h)
                    y %= h;
                if ((double) y < 0.0)
                    y = h + y;
            }
            if (this.seamlessZ)
            {
                if ((double) z >= (double) zl)
                    z %= zl;
                if ((double) z < 0.0)
                    z = zl + z;
            }
            return this.pseudoRandomFromPerm((int) x, (int) y, (int) z);
        }

        private float InterpolatedNoise_3d(float x, float y, float z, float w, float h, float zlv)
        {
            int num1 = (int) x;
            float x1 = x - (float) num1;
            int num2 = (int) y;
            float x2 = y - (float) num2;
            int num3 = (int) z;
            float x3 = z - (float) num3;
            int num4 = num1 + 1;
            int num5 = num2 + 1;
            int num6 = num3 + 1;
            float a1 = this.noise((float) num1, (float) num2, (float) num3, w, h, zlv);
            float b1 = this.noise((float) num4, (float) num2, (float) num3, w, h, zlv);
            float a2 = this.noise((float) num1, (float) num5, (float) num3, w, h, zlv);
            float b2 = this.noise((float) num4, (float) num5, (float) num3, w, h, zlv);
            float a3 = this.cos_Interpolate(this.cos_Interpolate(a1, b1, x1), this.cos_Interpolate(a2, b2, x1), x2);
            float a4 = this.noise((float) num1, (float) num2, (float) num6, w, h, zlv);
            float b3 = this.noise((float) num4, (float) num2, (float) num6, w, h, zlv);
            float a5 = this.noise((float) num1, (float) num5, (float) num6, w, h, zlv);
            float b4 = this.noise((float) num4, (float) num5, (float) num6, w, h, zlv);
            float b5 = this.cos_Interpolate(this.cos_Interpolate(a4, b3, x1), this.cos_Interpolate(a5, b4, x1), x2);
            return this.cos_Interpolate(a3, b5, x3);
        }

        private float fast_InterpolatedNoise_3d(float x, float y, float z, float w, float h, float zlv)
        {
            int num1 = (int) x;
            float x1 = x - (float) num1;
            int num2 = (int) y;
            float x2 = y - (float) num2;
            int num3 = (int) z;
            float x3 = z - (float) num3;
            int num4 = num1 + 1;
            int num5 = num2 + 1;
            int num6 = num3 + 1;
            float a1 = this.noise((float) num1, (float) num2, (float) num3, w, h, zlv);
            float b1 = this.noise((float) num4, (float) num2, (float) num3, w, h, zlv);
            float a2 = this.noise((float) num1, (float) num5, (float) num3, w, h, zlv);
            float b2 = this.noise((float) num4, (float) num5, (float) num3, w, h, zlv);
            float a3 = this.lin_Interpolate(this.lin_Interpolate(a1, b1, x1), this.lin_Interpolate(a2, b2, x1), x2);
            float a4 = this.noise((float) num1, (float) num2, (float) num6, w, h, zlv);
            float b3 = this.noise((float) num4, (float) num2, (float) num6, w, h, zlv);
            float a5 = this.noise((float) num1, (float) num5, (float) num6, w, h, zlv);
            float b4 = this.noise((float) num4, (float) num5, (float) num6, w, h, zlv);
            float b5 = this.lin_Interpolate(this.lin_Interpolate(a4, b3, x1), this.lin_Interpolate(a5, b4, x1), x2);
            return this.lin_Interpolate(a3, b5, x3);
        }

        private double PerlinNoise3d(int x, int y, int z, int W, int H, int ZLV, float x_frequency, float y_frequency,
            float z_frequency, float persistence, float octaves, float amplitude)
        {
            double num1 = 0.0;
            for (int index = 0; (double) index < (double) octaves; ++index)
            {
                if (this.fastPerlin)
                    num1 +=
                        (double)
                        this.fast_InterpolatedNoise_3d((float) x * x_frequency, (float) y * y_frequency,
                            (float) z * z_frequency, (float) W * x_frequency, (float) H * y_frequency,
                            (float) ZLV * z_frequency) * (double) amplitude;
                else
                    num1 +=
                        (double)
                        this.InterpolatedNoise_3d((float) x * x_frequency, (float) y * y_frequency,
                            (float) z * z_frequency, (float) W * x_frequency, (float) H * y_frequency,
                            (float) ZLV * z_frequency) * (double) amplitude;
                if (num1 <= 1.0)
                {
                    x_frequency *= 2f;
                    y_frequency *= 2f;
                    z_frequency *= 2f;
                    amplitude *= persistence;
                }
                else
                    break;
            }
            double num2 = (num1 - (double) this.cloudCoverage) * (double) this.cloudDensity;
            if (num2 < 0.0)
                num2 = 0.0;
            if (num2 > 1.0)
                num2 = 1.0;
            return num2;
        }

        public void PerlinNoise3d(float x_frequency, float y_frequency, float z_frequency, float persistence,
            float octaves, float amplitude)
        {
            x_frequency = 1f / x_frequency;
            y_frequency = 1f / y_frequency;
            z_frequency = 1f / z_frequency;
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < this.hei; ++index2)
                {
                    for (int index3 = 0; index3 < this.zlvl; ++index3)
                    {
                        double num = this.PerlinNoise3d(index1, index2, index3, this.wid, this.hei, this.zlvl,
                            x_frequency, y_frequency, z_frequency, persistence, octaves, amplitude);
                        this.setV(index1, index2, index3, (float) num * (float) byte.MaxValue);
                    }
                }
            }
        }

        public void cb_PerlinNoise(float x_frequency, float y_frequency, float z_frequency, float persistence,
            float octaves, float amplitude)
        {
            x_frequency = 1f / x_frequency;
            y_frequency = 1f / y_frequency;
            z_frequency = 1f / z_frequency;
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < this.hei; ++index2)
                {
                    double num1 = this.PerlinNoise3d(index1, index2, 0, this.wid, this.hei, this.zlvl, x_frequency,
                        y_frequency, z_frequency, persistence, octaves, amplitude);
                    this.setV(index1, index2, 0, (float) num1 * (float) byte.MaxValue);
                    double num2 = this.PerlinNoise3d(index1, index2, this.zlvl - 1, this.wid, this.hei, this.zlvl,
                        x_frequency, y_frequency, z_frequency, persistence, octaves, amplitude);
                    this.setV(index1, index2, this.zlvl - 1, (float) num2 * (float) byte.MaxValue);
                    double num3 = this.PerlinNoise3d(index1, 0, index2, this.wid, this.hei, this.zlvl, x_frequency,
                        y_frequency, z_frequency, persistence, octaves, amplitude);
                    this.setV(index1, 0, index2, (float) num3 * (float) byte.MaxValue);
                    double num4 = this.PerlinNoise3d(index1, this.hei - 1, index2, this.wid, this.hei, this.zlvl,
                        x_frequency, y_frequency, z_frequency, persistence, octaves, amplitude);
                    this.setV(index1, this.hei - 1, index2, (float) num4 * (float) byte.MaxValue);
                    double num5 = this.PerlinNoise3d(0, index2, index1, this.wid, this.hei, this.zlvl, x_frequency,
                        y_frequency, z_frequency, persistence, octaves, amplitude);
                    this.setV(0, index2, index1, (float) num5 * (float) byte.MaxValue);
                    double num6 = this.PerlinNoise3d(this.wid - 1, index2, index1, this.wid, this.hei, this.zlvl,
                        x_frequency, y_frequency, z_frequency, persistence, octaves, amplitude);
                    this.setV(this.wid - 1, index2, index1, (float) num6 * (float) byte.MaxValue);
                }
            }
        }

        public void QuadNoise(int repeat_N, int boxSizeW, int boxSizeH, double reductionFactor, int deepness,
            float delta)
        {
            if (boxSizeW > this.wid)
                boxSizeW = this.wid;
            if (boxSizeH > this.hei)
                boxSizeH = this.hei;
            for (int index = 0; index < repeat_N; ++index)
                this.SingleQuadNoise(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1), boxSizeW, boxSizeH, reductionFactor,
                    deepness, delta);
        }

        private void SingleQuadNoise(int x, int y, int boxSizeW, int boxSizeH, double reductionFactor, int deepness,
            float delta)
        {
            if (deepness <= 0 || boxSizeW < 1 || boxSizeH < 1)
                return;
            for (int i1 = -boxSizeW / 2; i1 < boxSizeW / 2; ++i1)
            {
                for (int i2 = -boxSizeH / 2; i2 < boxSizeH / 2; ++i2)
                {
                    int i3 = this.seamlessCoord(x, i1, this.wid, this.seamlessX);
                    int j = this.seamlessCoord(y, i2, this.hei, this.seamlessY);
                    if (this.coordisvalid(i3, j))
                        this.addV(i3, j, delta);
                }
                int boxSizeW1 = (int) ((double) boxSizeW * reductionFactor);
                int boxSizeH1 = (int) ((double) boxSizeH * reductionFactor);
                this.SingleQuadNoise(this.r.Next(x - boxSizeW1, x + boxSizeW1),
                    this.r.Next(y - boxSizeH1, y + boxSizeH1), boxSizeW1, boxSizeH1, reductionFactor, --deepness, delta);
            }
        }

        public void QuadNoise3D(int repeat_N, int boxSizeW, int boxSizeH, int boxSizeZ, double reductionFactor,
            int deepness, float delta)
        {
            if (boxSizeW > this.wid)
                boxSizeW = this.wid;
            if (boxSizeH > this.hei)
                boxSizeH = this.hei;
            if (boxSizeZ > this.zlvl)
                boxSizeZ = this.zlvl;
            for (int index = 0; index < repeat_N; ++index)
                this.SingleQuadNoise3D(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1),
                    this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1), boxSizeW, boxSizeH, boxSizeZ,
                    reductionFactor, deepness, delta);
        }

        private void SingleQuadNoise3D(int x, int y, int z, int boxSizeW, int boxSizeH, int boxSizeZ,
            double reductionFactor, int deepness, float delta)
        {
            for (int i1 = -boxSizeW / 2; i1 < boxSizeW / 2; ++i1)
            {
                for (int i2 = -boxSizeH / 2; i2 < boxSizeH / 2; ++i2)
                {
                    for (int i3 = -boxSizeZ / 2; i3 < boxSizeZ / 2; ++i3)
                    {
                        int i4 = this.seamlessCoord(x, i1, this.wid, this.seamlessX);
                        int j = this.seamlessCoord(y, i2, this.hei, this.seamlessY);
                        int k = this.seamlessCoord(z, i3, this.zlvl, this.seamlessZ);
                        if (this.coordisvalid(i4, j, k))
                            this.addV(i4, j, k, delta);
                    }
                }
                if (deepness > 1 && boxSizeW > 1 && (boxSizeH > 1 && boxSizeZ > 1))
                {
                    int boxSizeW1 = (int) ((double) boxSizeW * reductionFactor);
                    int boxSizeH1 = (int) ((double) boxSizeH * reductionFactor);
                    int boxSizeZ1 = (int) ((double) boxSizeZ * reductionFactor);
                    this.SingleQuadNoise3D(this.r.Next(x - boxSizeW1, x + boxSizeW1),
                        this.r.Next(y - boxSizeH1, y + boxSizeH1), this.r.Next(z - boxSizeZ1, z + boxSizeZ1), boxSizeW1,
                        boxSizeH1, boxSizeZ1, reductionFactor, --deepness, delta);
                }
            }
        }

        public void CircNoise(int repeat_N, int boxSize, double reductionFactor, int deepness, float delta)
        {
            if (boxSize > this.wid)
                boxSize = this.wid;
            if (boxSize > this.hei)
                boxSize = this.hei;
            for (int index = 0; index < repeat_N; ++index)
                this.SingleCircNoise(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1), boxSize, reductionFactor, deepness,
                    delta);
        }

        private void SingleCircNoise(int x, int y, int boxSize, double reductionFactor, int deepness, float delta)
        {
            int num1 = boxSize / 2;
            double num2 = Math.Pow((double) num1, 2.0);
            if (deepness <= 0 || boxSize < 1)
                return;
            for (int i1 = -num1; i1 < num1; ++i1)
            {
                for (int i2 = -num1; i2 < num1; ++i2)
                {
                    if ((double) (i1 * i1 + i2 * i2) <= num2)
                    {
                        int i3 = this.seamlessCoord(x, i1, this.wid, this.seamlessX);
                        int j = this.seamlessCoord(y, i2, this.hei, this.seamlessY);
                        if (this.coordisvalid(i3, j))
                            this.addV(i3, j, delta);
                    }
                }
                int boxSize1 = (int) ((double) boxSize * reductionFactor);
                this.SingleCircNoise(this.r.Next(x - boxSize1, x + boxSize1), this.r.Next(y - boxSize1, y + boxSize1),
                    boxSize1, reductionFactor, --deepness, delta);
            }
        }

        public void CircNoise3D(int repeat_N, int boxSize, double reductionFactor, int deepness, float delta)
        {
            if (boxSize > this.wid)
                boxSize = this.wid;
            if (boxSize > this.hei)
                boxSize = this.hei;
            if (boxSize > this.zlvl)
                boxSize = this.zlvl;
            for (int index = 0; index < repeat_N; ++index)
                this.SingleCircNoise3D(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1),
                    this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1), boxSize, reductionFactor, deepness,
                    delta);
        }

        private void SingleCircNoise3D(int x, int y, int z, int boxSize, double reductionFactor, int deepness,
            float delta)
        {
            int num1 = boxSize / 2;
            double num2 = Math.Pow((double) num1, 2.0);
            for (int i1 = -num1; i1 < num1; ++i1)
            {
                for (int i2 = -num1; i2 < num1; ++i2)
                {
                    for (int i3 = -num1; i3 < num1; ++i3)
                    {
                        if ((double) (i1 * i1 + i2 * i2 + i3 * i3) <= num2)
                        {
                            int i4 = this.seamlessCoord(x, i1, this.wid, this.seamlessX);
                            int j = this.seamlessCoord(y, i2, this.hei, this.seamlessY);
                            int k = this.seamlessCoord(z, i3, this.zlvl, this.seamlessZ);
                            if (this.coordisvalid(i4, j, k))
                                this.addV(i4, j, k, delta);
                        }
                    }
                }
                if (deepness > 1 && boxSize > 1)
                {
                    int boxSize1 = (int) ((double) boxSize * reductionFactor);
                    this.SingleCircNoise3D(this.r.Next(x - boxSize1, x + boxSize1),
                        this.r.Next(y - boxSize1, y + boxSize1), this.r.Next(z - boxSize1, z + boxSize1), boxSize1,
                        reductionFactor, --deepness, delta);
                }
            }
        }

        public void F_Noise(int repeat_N, int boxSizeW, int boxSizeH, double reductionFactor, int deepness, float delta)
        {
            if (boxSizeW > this.wid)
                boxSizeW = this.wid;
            if (boxSizeH > this.hei)
                boxSizeH = this.wid;
            for (int index = 0; index < repeat_N; ++index)
                this.SingleF_Noise(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1), boxSizeW, boxSizeH, reductionFactor,
                    deepness, delta, this.ff);
        }

        private void SingleF_Noise(int x, int y, int boxSizeW, int boxSizeH, double reductionFactor, int deepness,
            float delta, cnt_f f)
        {
            if (deepness <= 0 || boxSizeW < 1 || boxSizeH < 1)
                return;
            int num1 = boxSizeW / 2;
            int num2 = boxSizeH / 2;
            float num3 = (float) boxSizeW / 2f;
            float num4 = (float) boxSizeH / 2f;
            for (int i1 = -num1; i1 < num1; ++i1)
            {
                for (int i2 = -num2; i2 < num2; ++i2)
                {
                    float x1 = (float) i1 / num3;
                    float y1 = (float) i2 / num4;
                    float v = delta * f(x1, y1);
                    if ((double) v > 0.0)
                    {
                        int i3 = this.seamlessCoord(x, i1, this.wid, this.seamlessX);
                        int j = this.seamlessCoord(y, i2, this.hei, this.seamlessY);
                        if (this.coordisvalid(i3, j))
                            this.addV(i3, j, v);
                    }
                }
                int boxSizeW1 = (int) ((double) boxSizeW * reductionFactor);
                int boxSizeH1 = (int) ((double) boxSizeH * reductionFactor);
                this.SingleF_Noise(this.r.Next(x - boxSizeW1, x + boxSizeW1), this.r.Next(y - boxSizeH1, y + boxSizeH1),
                    boxSizeW1, boxSizeH1, reductionFactor, --deepness, delta, this.ff);
            }
        }

        public void F_Noise3D(int repeat_N, int boxSizeW, int boxSizeH, int boxSizeZ, double reductionFactor,
            int deepness, float delta)
        {
            if (this.n_dimensions != 3)
                return;
            if (boxSizeW > this.wid)
                boxSizeW = this.wid;
            if (boxSizeH > this.hei)
                boxSizeH = this.hei;
            if (boxSizeZ > this.zlvl)
                boxSizeZ = this.zlvl;
            for (int index = 0; index < repeat_N; ++index)
                this.SingleF_Noise3D(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1),
                    this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1), boxSizeW, boxSizeH, boxSizeZ,
                    reductionFactor, deepness, delta, this.ff3d);
        }

        private void SingleF_Noise3D(int x, int y, int z, int boxSizeW, int boxSizeH, int boxSizeZ,
            double reductionFactor, int deepness, float delta, cnt_f3D f)
        {
            int num1 = boxSizeW / 2;
            int num2 = boxSizeH / 2;
            int num3 = boxSizeZ / 2;
            float num4 = (float) boxSizeW / 2f;
            float num5 = (float) boxSizeH / 2f;
            float num6 = (float) boxSizeZ / 2f;
            for (int i1 = -num1; i1 < num1; ++i1)
            {
                for (int i2 = -num2; i2 < num2; ++i2)
                {
                    for (int i3 = -num3; i3 < num3; ++i3)
                    {
                        float x1 = (float) i1 / num4;
                        float y1 = (float) i2 / num5;
                        float z1 = (float) i3 / num6;
                        float v = delta * f(x1, y1, z1);
                        if ((double) v > 0.0)
                        {
                            int i4 = this.seamlessCoord(x, i1, this.wid, this.seamlessX);
                            int j = this.seamlessCoord(y, i2, this.hei, this.seamlessY);
                            int k = this.seamlessCoord(z, i3, this.zlvl, this.seamlessZ);
                            if (this.coordisvalid(i4, j, k))
                                this.addV(i4, j, k, v);
                        }
                    }
                }
                if (deepness > 1 && boxSizeW > 1 && (boxSizeH > 1 && boxSizeZ > 1))
                {
                    int boxSizeW1 = (int) ((double) boxSizeW * reductionFactor);
                    int boxSizeH1 = (int) ((double) boxSizeH * reductionFactor);
                    int boxSizeZ1 = (int) ((double) boxSizeZ * reductionFactor);
                    this.SingleF_Noise3D(this.r.Next(x - boxSizeW1, x + boxSizeW1),
                        this.r.Next(y - boxSizeH1, y + boxSizeH1), this.r.Next(z - boxSizeZ1, z + boxSizeZ1), boxSizeW1,
                        boxSizeH1, boxSizeZ1, reductionFactor, --deepness, delta, f);
                }
            }
        }

        public void F_Noise1D(int repeat_N, int boxSizeW, double reductionFactor, int deepness, int delta)
        {
            if (boxSizeW > this.wid)
                boxSizeW = this.wid;
            for (int index = 0; index < repeat_N; ++index)
                this.SingleF_Noise1D(this.r.Next(0, this.wid - 1), boxSizeW, reductionFactor, deepness, delta, this.ff1d);
        }

        private void SingleF_Noise1D(int x, int boxSizeW, double reductionFactor, int deepness, int delta, cnt_f1D f)
        {
            if (deepness <= 0 || boxSizeW < 1)
                return;
            int num1 = boxSizeW / 2;
            float num2 = (float) boxSizeW / 2f;
            for (int i1 = -num1; i1 < num1; ++i1)
            {
                float x1 = (float) i1 / num2;
                float v = (float) delta * f(x1);
                if ((double) v > 0.0)
                {
                    int i2 = this.seamlessCoord(x, i1, this.wid, this.seamlessX);
                    if (this.coordisvalid(i2))
                        this.addV(i2, v);
                }
                int boxSizeW1 = (int) ((double) boxSizeW * reductionFactor);
                this.SingleF_Noise1D(this.r.Next(x - boxSizeW1, x + boxSizeW1), boxSizeW1, reductionFactor, --deepness,
                    delta, f);
            }
        }

        public void cb_Noise3D(int repeat_N, int boxSizeW, int boxSizeH, int boxSizeZ, double reductionFactor,
            int deepness, float delta)
        {
            if (this.n_dimensions != 3)
                return;
            if (boxSizeW > this.wid)
                boxSizeW = this.wid;
            if (boxSizeH > this.hei)
                boxSizeH = this.hei;
            if (boxSizeZ > this.zlvl)
                boxSizeZ = this.zlvl;
            for (int index = 0; index < repeat_N; ++index)
            {
                int x = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                int y = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                int z = this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1);
                int face = this.r.Next(0, 6);
                switch (face)
                {
                    case 0:
                        z = this.startMarginZ;
                        break;
                    case 1:
                        z = this.zlvl - this.endMarginZ - 1;
                        break;
                    case 2:
                        x = this.startMarginX;
                        break;
                    case 3:
                        x = this.wid - this.endMarginX - 1;
                        break;
                    case 4:
                        y = this.startMarginY;
                        break;
                    case 5:
                        y = this.hei - this.endMarginY - 1;
                        break;
                }
                this.cb_SingleF_Noise3D(x, y, z, boxSizeW, boxSizeH, boxSizeZ, reductionFactor, deepness, delta,
                    this.ff3d, face);
            }
        }

        private void cb_SingleF_Noise3D(int x, int y, int z, int boxSizeW, int boxSizeH, int boxSizeZ,
            double reductionFactor, int deepness, float delta, cnt_f3D f, int face)
        {
            int num1 = boxSizeW / 2;
            int num2 = boxSizeH / 2;
            int num3 = boxSizeZ / 2;
            float num4 = (float) boxSizeW / 2f;
            float num5 = (float) boxSizeH / 2f;
            float num6 = (float) boxSizeZ / 2f;
            for (int i1 = -num1; i1 < num1; ++i1)
            {
                for (int i2 = -num2; i2 < num2; ++i2)
                {
                    for (int i3 = -num3; i3 < num3; ++i3)
                    {
                        if (x + i1 == 0 || y + i2 == 0 || (z + i3 == 0 || x + i1 == this.wid - 1) ||
                            (y + i2 == this.hei - 1 || z + i3 == this.zlvl - 1))
                        {
                            float x1 = (float) i1 / num4;
                            float y1 = (float) i2 / num5;
                            float z1 = (float) i3 / num6;
                            float v = delta * f(x1, y1, z1);
                            if ((double) v > 0.0)
                            {
                                int i4 = this.seamlessCoord(x, i1, this.wid, this.seamlessX);
                                int j = this.seamlessCoord(y, i2, this.hei, this.seamlessY);
                                int k = this.seamlessCoord(z, i3, this.zlvl, this.seamlessZ);
                                if (this.coordisvalid(i4, j, k))
                                    this.addV(i4, j, k, v);
                            }
                        }
                    }
                }
                if (deepness > 1 && boxSizeW > 1 && (boxSizeH > 1 && boxSizeZ > 1))
                {
                    int boxSizeW1 = (int) ((double) boxSizeW * reductionFactor);
                    int boxSizeH1 = (int) ((double) boxSizeH * reductionFactor);
                    int boxSizeZ1 = (int) ((double) boxSizeZ * reductionFactor);
                    int x1 = this.r.Next(x - boxSizeW1, x + boxSizeW1);
                    int y1 = this.r.Next(y - boxSizeH1, y + boxSizeH1);
                    int z1 = this.r.Next(z - boxSizeZ1, z + boxSizeZ1);
                    switch (face)
                    {
                        case 0:
                            z1 = z;
                            break;
                        case 1:
                            z1 = z;
                            break;
                        case 2:
                            x1 = x;
                            break;
                        case 3:
                            x1 = x;
                            break;
                        case 4:
                            y1 = y;
                            break;
                        case 5:
                            y1 = y;
                            break;
                    }
                    this.cb_SingleF_Noise3D(x1, y1, z1, boxSizeW1, boxSizeH1, boxSizeZ1, reductionFactor, --deepness,
                        delta, f, face);
                }
            }
        }

        public void Distance(int N_Repetitions, int minDelta, int minDeltaTo, float WaveFactor)
        {
            if (this.ptsDistr != null)
            {
                for (int index = 0; index < this.ptsDistr.GetLength(0); ++index)
                    this.SingleDistance(this.ptsDistr[index, 0], this.ptsDistr[index, 1], minDelta, minDeltaTo,
                        WaveFactor);
            }
            else
            {
                for (int index = 0; index < N_Repetitions; ++index)
                    this.SingleDistance(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                        this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1), minDelta, minDeltaTo, WaveFactor);
            }
        }

        private void SingleDistance(int x, int y, int fromR, int toR, float WaveFactor)
        {
            int num1 = this.r.Next(fromR, toR);
            for (int index1 = x - num1; index1 < x + num1; ++index1)
            {
                for (int index2 = y - num1; index2 < y + num1; ++index2)
                {
                    int i = index1 % this.wid;
                    if (i < 0)
                        i = this.wid + i;
                    int j = index2 % this.hei;
                    if (j < 0)
                        j = this.hei + j;
                    double val1_1 = (double) Math.Abs(i - x);
                    double val1_2 = (double) Math.Abs(j - y);
                    double val2_1 = (double) this.wid - val1_1;
                    double val2_2 = (double) this.hei - val1_2;
                    double num2 = Math.Min(val1_1, val2_1);
                    double num3 = Math.Min(val1_2, val2_2);
                    double a = Math.Sqrt(num2 * num2 + num3 * num3);
                    float num4 = (float) (a + Math.Sin(a) * (double) WaveFactor) / (float) num1;
                    if ((double) num4 > 1.0)
                        num4 = 1f;
                    float v1 = (float) ((double) byte.MaxValue - (double) byte.MaxValue * (double) num4);
                    if ((double) v1 <= (double) byte.MaxValue && (double) v1 != 0.0)
                    {
                        if ((double) this.getV(i, j) > 0.0)
                        {
                            if ((double) this.getV(i, j) >= (double) v1)
                            {
                                this.addV(i, j, -v1);
                            }
                            else
                            {
                                float v2 = this.getV(i, j);
                                this.setV(i, j, (float) ((double) v1 - (double) v2 - 1.0));
                            }
                        }
                        else
                            this.setV(i, j, v1);
                    }
                }
            }
        }

        public void Distance3D(int N_Repetitions, int minDelta, int minDeltaTo, float WaveFactor)
        {
            int fromR = minDelta;
            if (this.zlvl < minDelta)
                fromR = this.zlvl;
            if (this.ptsDistr != null)
            {
                for (int index = 0; index < this.ptsDistr.GetLength(0); ++index)
                    this.SingleDistance3D(this.ptsDistr[index, 0], this.ptsDistr[index, 1], this.ptsDistr[index, 2],
                        fromR, minDeltaTo, WaveFactor);
            }
            else
            {
                for (int index = 0; index < N_Repetitions; ++index)
                    this.SingleDistance3D(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                        this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1),
                        this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1), fromR, minDeltaTo, WaveFactor);
            }
        }

        public void cb_Distance(int N_Repetitions, int minDelta, int minDeltaTo, float WaveFactor)
        {
            int fromR = minDelta;
            if (this.zlvl < minDelta)
                fromR = this.zlvl;
            for (int index = 0; index < N_Repetitions; ++index)
            {
                int x = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                int y = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                int z = this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1);
                switch (this.r.Next(0, 6))
                {
                    case 0:
                        z = this.startMarginZ;
                        break;
                    case 1:
                        z = this.zlvl - this.endMarginZ - 1;
                        break;
                    case 2:
                        x = this.startMarginX;
                        break;
                    case 3:
                        x = this.wid - this.endMarginX - 1;
                        break;
                    case 4:
                        y = this.startMarginY;
                        break;
                    case 5:
                        y = this.hei - this.endMarginY - 1;
                        break;
                }
                this.cb_SingleDistance(x, y, z, fromR, minDeltaTo, WaveFactor);
            }
        }

        private void cb_SingleDistance(int x, int y, int z, int fromR, int toR, float WaveFactor)
        {
            int num1 = this.r.Next(fromR, toR);
            int num2 = this.wid - this.endMarginX - 1;
            int num3 = this.hei - this.endMarginY - 1;
            int num4 = this.zlvl - this.endMarginZ - 1;
            int num5 = Math.Max(0, x - num1);
            int num6 = Math.Max(0, y - num1);
            int num7 = Math.Max(0, z - num1);
            int num8 = Math.Min(this.wid, x + num1);
            int num9 = Math.Min(this.hei, y + num1);
            int num10 = Math.Min(this.zlvl, z + num1);
            for (int index1 = num5; index1 < num8; ++index1)
            {
                for (int index2 = num6; index2 < num9; ++index2)
                {
                    for (int index3 = num7; index3 < num10; ++index3)
                    {
                        if (index1 == this.startMarginX || index2 == this.startMarginY ||
                            (index3 == this.startMarginZ || index1 == num2) || (index2 == num3 || index3 == num4))
                        {
                            int i = index1;
                            int j = index2;
                            int k = index3;
                            double num11 = (double) Math.Abs(i - x);
                            double num12 = (double) Math.Abs(j - y);
                            double num13 = (double) Math.Abs(k - z);
                            double a = Math.Sqrt(num11 * num11 + num12 * num12 + num13 * num13);
                            float num14 = (float) (a + Math.Sin(a) * (double) WaveFactor) / (float) num1;
                            if ((double) num14 > 1.0)
                                num14 = 1f;
                            float v = (float) ((double) byte.MaxValue - (double) byte.MaxValue * (double) num14);
                            if ((double) v <= (double) byte.MaxValue && (double) v != 0.0)
                            {
                                if ((double) this.getV(i, j, k) > 0.0)
                                {
                                    if ((double) this.getV(i, j, k) >= (double) v)
                                        this.addV(i, j, k, -v);
                                    else
                                        this.setV(i, j, k, (float) ((double) v - (double) this.getV(i, j, k) - 1.0));
                                }
                                else
                                    this.setV(i, j, k, v);
                            }
                        }
                    }
                }
            }
        }

        private void SingleDistance3D(int x, int y, int z, int fromR, int toR, float WaveFactor)
        {
            int num1 = this.r.Next(fromR, toR);
            for (int index1 = x - num1; index1 < x + num1; ++index1)
            {
                for (int index2 = y - num1; index2 < y + num1; ++index2)
                {
                    for (int index3 = z - num1; index3 < z + num1; ++index3)
                    {
                        int i = index1 % this.wid;
                        if (i < 0)
                            i = this.wid + i;
                        int j = index2 % this.hei;
                        if (j < 0)
                            j = this.hei + j;
                        int k = index3 % this.zlvl;
                        if (k < 0)
                            k = this.zlvl + k;
                        double val1_1 = (double) Math.Abs(i - x);
                        double val1_2 = (double) Math.Abs(j - y);
                        double val1_3 = (double) Math.Abs(k - z);
                        double val2_1 = (double) this.wid - val1_1;
                        double val2_2 = (double) this.hei - val1_2;
                        double val2_3 = (double) this.zlvl - val1_3;
                        double num2 = Math.Min(val1_1, val2_1);
                        double num3 = Math.Min(val1_2, val2_2);
                        double num4 = Math.Min(val1_3, val2_3);
                        double a = Math.Sqrt(num2 * num2 + num3 * num3 + num4 * num4);
                        float num5 = (float) (a + Math.Sin(a) * (double) WaveFactor) / (float) num1;
                        if ((double) num5 > 1.0)
                            num5 = 1f;
                        float v = (float) ((double) byte.MaxValue - (double) byte.MaxValue * (double) num5);
                        if ((double) v <= (double) byte.MaxValue && (double) v != 0.0)
                        {
                            if ((double) this.getV(i, j, k) > 0.0)
                            {
                                if ((double) this.getV(i, j, k) >= (double) v)
                                    this.addV(i, j, k, -v);
                                else
                                    this.setV(i, j, k, (float) ((double) v - (double) this.getV(i, j, k) - 1.0));
                            }
                            else
                                this.setV(i, j, k, v);
                        }
                    }
                }
            }
        }

        public void Voronoi(int n, int mindelta, float WaveFactor, float WaveAmp)
        {
            if (this.ptsDistr != null)
            {
                this.Voronoi(this.ptsDistr, mindelta, WaveFactor, WaveAmp);
            }
            else
            {
                int[,] points = new int[n, 3];
                for (int index = 0; index < n; ++index)
                {
                    points[index, 0] = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                    points[index, 1] = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                    points[index, 2] = this.r.Next(0, (int) byte.MaxValue);
                }
                this.Voronoi(points, mindelta, WaveFactor, WaveAmp);
            }
        }

        private void Voronoi(int[,] points, int minDelta, float WaveFactor, float WaveAmp)
        {
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    float num1 = 1E+09f;
                    float num2 = 1E+09f;
                    int num3 = 0;
                    for (int index = 0; index < points.GetLength(0); ++index)
                    {
                        int point1 = points[index, 0];
                        int point2 = points[index, 1];
                        int point3 = points[index, 2];
                        double val1_1 = (double) Math.Abs(i - point1);
                        double val1_2 = (double) Math.Abs(j - point2);
                        double val2_1 = (double) this.wid - val1_1;
                        double val2_2 = (double) this.hei - val1_2;
                        double num4 = Math.Min(val1_1, val2_1);
                        double num5 = Math.Min(val1_2, val2_2);
                        float num6 = (float) Math.Sqrt(num4 * num4 + num5 * num5);
                        if ((double) WaveAmp != 0.0)
                            num6 += (float) Math.Sin((double) num6 * (double) WaveFactor) * WaveAmp;
                        if ((double) num6 <= (double) num1)
                        {
                            num2 = num1;
                            num1 = num6;
                            num3 = point3;
                        }
                        else if ((double) num6 <= (double) num2)
                            num2 = num6;
                    }
                    if ((double) num2 - (double) num1 < 1.0)
                        this.setV(i, j, 0.0f);
                    else if ((double) num2 - (double) num1 < (double) minDelta)
                        this.setV(i, j, 0.0f);
                    else
                        this.setV(i, j, (float) num3);
                }
            }
        }

        public void Voronoi3(int n, int mindelta, float WaveFactor, float WaveAmp)
        {
            if (this.ptsDistr != null)
            {
                this.Voronoi3(this.ptsDistr, mindelta, WaveFactor, WaveAmp);
            }
            else
            {
                int[,] points = new int[n, 3];
                for (int index = 0; index < n; ++index)
                {
                    points[index, 0] = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                    points[index, 1] = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                    points[index, 2] = this.r.Next(0, (int) byte.MaxValue);
                }
                this.Voronoi3(points, mindelta, WaveFactor, WaveAmp);
            }
        }

        private void Voronoi3(int[,] points, int minDelta, float WaveFactor, float WaveAmp)
        {
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    float num1 = 1E+09f;
                    float num2 = 1E+09f;
                    for (int index = 0; index < points.GetLength(0); ++index)
                    {
                        int point1 = points[index, 0];
                        int point2 = points[index, 1];
                        int point3 = points[index, 2];
                        double val1_1 = (double) Math.Abs(i - point1);
                        double val1_2 = (double) Math.Abs(j - point2);
                        double val2_1 = (double) this.wid - val1_1;
                        double val2_2 = (double) this.hei - val1_2;
                        double num3 = Math.Min(val1_1, val2_1);
                        double num4 = Math.Min(val1_2, val2_2);
                        float num5 = (float) Math.Sqrt(num3 * num3 + num4 * num4);
                        if ((double) WaveAmp != 0.0)
                            num5 += (float) Math.Sin((double) num5 * (double) WaveFactor) * WaveAmp;
                        if ((double) num5 <= (double) num1)
                        {
                            num2 = num1;
                            num1 = num5;
                        }
                        else if ((double) num5 <= (double) num2)
                            num2 = num5;
                    }
                    if ((double) num2 - (double) num1 < 1.0)
                        this.setV(i, j, 0.0f);
                    else if ((double) num2 - (double) num1 < (double) minDelta)
                        this.setV(i, j,
                            (float) (((double) num2 - (double) num1) / (double) minDelta * (double) byte.MaxValue));
                    else
                        this.setV(i, j, (float) byte.MaxValue);
                }
            }
        }

        public void Voronoi3_3d(int n, int minDelta, float WaveFactor, float WaveAmp)
        {
            if (this.ptsDistr != null)
            {
                this.Voronoi3_3D(this.ptsDistr, minDelta, WaveFactor, WaveAmp);
            }
            else
            {
                int[,] points = new int[n, 4];
                for (int index = 0; index < n; ++index)
                {
                    points[index, 0] = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                    points[index, 1] = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                    points[index, 2] = this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1);
                    points[index, 3] = this.r.Next(0, (int) byte.MaxValue);
                }
                this.Voronoi3_3D(points, minDelta, WaveFactor, WaveAmp);
            }
        }

        private void Voronoi3_3D(int[,] points, int minDelta, float WaveFactor, float WaveAmp)
        {
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    for (int k = 0; k < this.zlvl; ++k)
                    {
                        float num1 = 1E+09f;
                        float num2 = 1E+09f;
                        for (int index = 0; index < points.GetLength(0); ++index)
                        {
                            int point1 = points[index, 0];
                            int point2 = points[index, 1];
                            int point3 = points[index, 2];
                            int point4 = points[index, 3];
                            double val1_1 = (double) Math.Abs(i - point1);
                            double val1_2 = (double) Math.Abs(j - point2);
                            double val1_3 = (double) Math.Abs(k - point3);
                            double val2_1 = (double) this.wid - val1_1;
                            double val2_2 = (double) this.hei - val1_2;
                            double val2_3 = (double) this.zlvl - val1_3;
                            double num3 = Math.Min(val1_1, val2_1);
                            double num4 = Math.Min(val1_2, val2_2);
                            double num5 = Math.Min(val1_3, val2_3);
                            float num6 = (float) Math.Sqrt(num3 * num3 + num4 * num4 + num5 * num5);
                            if ((double) WaveAmp != 0.0)
                                num6 += (float) Math.Sin((double) num6 * (double) WaveFactor) * WaveAmp;
                            if ((double) num6 <= (double) num1)
                            {
                                num2 = num1;
                                num1 = num6;
                            }
                            else if ((double) num6 <= (double) num2)
                                num2 = num6;
                        }
                        if ((double) num2 - (double) num1 < 1.0)
                            this.setV(i, j, k, 0.0f);
                        else if ((double) num2 - (double) num1 < (double) minDelta)
                            this.setV(i, j, k,
                                (float) (((double) num2 - (double) num1) / (double) minDelta * (double) byte.MaxValue));
                        else
                            this.setV(i, j, k, (float) byte.MaxValue);
                    }
                }
            }
        }

        public void cb_Voronoi3(int n, int minDelta, float WaveFactor, float WaveAmp)
        {
            int[,] points = new int[n, 4];
            for (int index = 0; index < n; ++index)
            {
                points[index, 0] = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                points[index, 1] = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                points[index, 2] = this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1);
                switch (this.r.Next(0, 6))
                {
                    case 0:
                        points[index, 2] = this.startMarginZ;
                        break;
                    case 1:
                        points[index, 2] = this.zlvl - this.endMarginZ - 1;
                        break;
                    case 2:
                        points[index, 0] = this.startMarginX;
                        break;
                    case 3:
                        points[index, 0] = this.wid - this.endMarginX - 1;
                        break;
                    case 4:
                        points[index, 1] = this.startMarginY;
                        break;
                    case 5:
                        points[index, 1] = this.hei - this.endMarginY - 1;
                        break;
                }
                points[index, 3] = this.r.Next(0, (int) byte.MaxValue);
            }
            this.cb_Voronoi3(points, minDelta, WaveFactor, WaveAmp);
        }

        private void cb_Voronoi3(int[,] points, int minDelta, float WaveFactor, float WaveAmp)
        {
            int num1 = this.wid - this.endMarginX - 1;
            int num2 = this.hei - this.endMarginY - 1;
            int num3 = this.zlvl - this.endMarginZ - 1;
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    for (int k = 0; k < this.zlvl; ++k)
                    {
                        if (i == this.startMarginX || j == this.startMarginY || (k == this.startMarginZ || i == num1) ||
                            (j == num2 || k == num3))
                        {
                            float num4 = 1E+09f;
                            float num5 = 1E+09f;
                            for (int index = 0; index < points.GetLength(0); ++index)
                            {
                                int point1 = points[index, 0];
                                int point2 = points[index, 1];
                                int point3 = points[index, 2];
                                int point4 = points[index, 3];
                                double val1_1 = (double) Math.Abs(i - point1);
                                double val1_2 = (double) Math.Abs(j - point2);
                                double val1_3 = (double) Math.Abs(k - point3);
                                double val2_1 = (double) this.wid - val1_1;
                                double val2_2 = (double) this.hei - val1_2;
                                double val2_3 = (double) this.zlvl - val1_3;
                                double num6 = Math.Min(val1_1, val2_1);
                                double num7 = Math.Min(val1_2, val2_2);
                                double num8 = Math.Min(val1_3, val2_3);
                                float num9 = (float) Math.Sqrt(num6 * num6 + num7 * num7 + num8 * num8);
                                if ((double) WaveAmp != 0.0)
                                    num9 += (float) Math.Sin((double) num9 * (double) WaveFactor) * WaveAmp;
                                if ((double) num9 <= (double) num4)
                                {
                                    num5 = num4;
                                    num4 = num9;
                                }
                                else if ((double) num9 <= (double) num5)
                                    num5 = num9;
                            }
                            if ((double) num5 - (double) num4 < 1.0)
                                this.setV(i, j, k, 0.0f);
                            else if ((double) num5 - (double) num4 < (double) minDelta)
                                this.setV(i, j, k,
                                    (float)
                                    (((double) num5 - (double) num4) / (double) minDelta * (double) byte.MaxValue));
                            else
                                this.setV(i, j, k, (float) byte.MaxValue);
                        }
                    }
                }
            }
        }

        public void Voronoi4(int n, int mindelta, float WaveFactor, float WaveAmp)
        {
            if (this.ptsDistr != null)
            {
                this.Voronoi4(this.ptsDistr, mindelta, WaveFactor, WaveAmp);
            }
            else
            {
                int[,] points = new int[n, 3];
                for (int index = 0; index < n; ++index)
                {
                    points[index, 0] = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                    points[index, 1] = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                    points[index, 2] = this.r.Next(0, (int) byte.MaxValue);
                }
                this.Voronoi4(points, mindelta, WaveFactor, WaveAmp);
            }
        }

        private void Voronoi4(int[,] points, int minDelta, float WaveFactor, float WaveAmp)
        {
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    float num1 = 1E+09f;
                    float num2 = 1E+09f;
                    float num3 = 1E+09f;
                    for (int index = 0; index < points.GetLength(0); ++index)
                    {
                        int point1 = points[index, 0];
                        int point2 = points[index, 1];
                        int point3 = points[index, 2];
                        double val1_1 = (double) Math.Abs(i - point1);
                        double val1_2 = (double) Math.Abs(j - point2);
                        double val2_1 = (double) this.wid - val1_1;
                        double val2_2 = (double) this.hei - val1_2;
                        double num4 = Math.Min(val1_1, val2_1);
                        double num5 = Math.Min(val1_2, val2_2);
                        float num6 = (float) Math.Sqrt(num4 * num4 + num5 * num5);
                        if ((double) WaveAmp != 0.0)
                            num6 += (float) Math.Sin((double) num6 * (double) WaveFactor) * WaveAmp;
                        if ((double) num6 <= (double) num1)
                        {
                            num3 = num2;
                            num2 = num1;
                            num1 = num6;
                        }
                        else if ((double) num6 <= (double) num2)
                        {
                            num3 = num2;
                            num2 = num6;
                        }
                        else if ((double) num6 <= (double) num3)
                            num3 = num6;
                    }
                    if ((double) num2 - (double) num1 + ((double) num3 - (double) num1) < 1.0)
                        this.setV(i, j, 0.0f);
                    else if ((double) num2 - (double) num1 + ((double) num3 - (double) num1) < (double) minDelta)
                        this.setV(i, j,
                            (float)
                            (((double) num2 - (double) num1 + ((double) num3 - (double) num1)) / (double) minDelta *
                             (double) byte.MaxValue));
                    else
                        this.setV(i, j, (float) byte.MaxValue);
                }
            }
        }

        public void Voronoi4_3d(int n, int minDelta, float WaveFactor, float WaveAmp)
        {
            if (this.ptsDistr != null)
            {
                this.Voronoi4_3D(this.ptsDistr, minDelta, WaveFactor, WaveAmp);
            }
            else
            {
                int[,] points = new int[n, 4];
                for (int index = 0; index < n; ++index)
                {
                    points[index, 0] = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                    points[index, 1] = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                    points[index, 2] = this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1);
                    points[index, 3] = this.r.Next(0, (int) byte.MaxValue);
                }
                this.Voronoi4_3D(points, minDelta, WaveFactor, WaveAmp);
            }
        }

        public void cb_Voronoi4(int n, int minDelta, float WaveFactor, float WaveAmp)
        {
            int[,] points = new int[n, 4];
            for (int index = 0; index < n; ++index)
            {
                points[index, 0] = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                points[index, 1] = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                points[index, 2] = this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1);
                switch (this.r.Next(0, 6))
                {
                    case 0:
                        points[index, 2] = this.startMarginZ;
                        break;
                    case 1:
                        points[index, 2] = this.zlvl - this.endMarginZ - 1;
                        break;
                    case 2:
                        points[index, 0] = this.startMarginX;
                        break;
                    case 3:
                        points[index, 0] = this.wid - this.endMarginX - 1;
                        break;
                    case 4:
                        points[index, 1] = this.startMarginY;
                        break;
                    case 5:
                        points[index, 1] = this.hei - this.endMarginY - 1;
                        break;
                }
                points[index, 3] = this.r.Next(0, (int) byte.MaxValue);
            }
            this.cb_Voronoi4(points, minDelta, WaveFactor, WaveAmp);
        }

        private void Voronoi4_3D(int[,] points, int minDelta, float WaveFactor, float WaveAmp)
        {
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    for (int k = 0; k < this.zlvl; ++k)
                    {
                        float num1 = 1E+09f;
                        float num2 = 1E+09f;
                        float num3 = 1E+09f;
                        for (int index = 0; index < points.GetLength(0); ++index)
                        {
                            int point1 = points[index, 0];
                            int point2 = points[index, 1];
                            int point3 = points[index, 2];
                            int point4 = points[index, 3];
                            double val1_1 = (double) Math.Abs(i - point1);
                            double val1_2 = (double) Math.Abs(j - point2);
                            double val1_3 = (double) Math.Abs(k - point3);
                            double val2_1 = (double) this.wid - val1_1;
                            double val2_2 = (double) this.hei - val1_2;
                            double val2_3 = (double) this.zlvl - val1_3;
                            double num4 = Math.Min(val1_1, val2_1);
                            double num5 = Math.Min(val1_2, val2_2);
                            double num6 = Math.Min(val1_3, val2_3);
                            float num7 = (float) Math.Sqrt(num4 * num4 + num5 * num5 + num6 * num6);
                            if ((double) WaveAmp != 0.0)
                                num7 += (float) Math.Sin((double) num7 * (double) WaveFactor) * WaveAmp;
                            if ((double) num7 <= (double) num1)
                            {
                                num3 = num2;
                                num2 = num1;
                                num1 = num7;
                            }
                            else if ((double) num7 <= (double) num2)
                            {
                                num3 = num2;
                                num2 = num7;
                            }
                            else if ((double) num7 <= (double) num3)
                                num3 = num7;
                        }
                        if ((double) num2 - (double) num1 + ((double) num3 - (double) num1) < 1.0)
                            this.setV(i, j, k, 0.0f);
                        else if ((double) num2 - (double) num1 + ((double) num3 - (double) num1) < (double) minDelta)
                            this.setV(i, j, k,
                                (float)
                                (((double) num2 - (double) num1 + ((double) num3 - (double) num1)) / (double) minDelta *
                                 (double) byte.MaxValue));
                        else
                            this.setV(i, j, k, (float) byte.MaxValue);
                    }
                }
            }
        }

        private void cb_Voronoi4(int[,] points, int minDelta, float WaveFactor, float WaveAmp)
        {
            int num1 = this.wid - this.endMarginX - 1;
            int num2 = this.hei - this.endMarginY - 1;
            int num3 = this.zlvl - this.endMarginZ - 1;
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    for (int k = 0; k < this.zlvl; ++k)
                    {
                        if (i == this.startMarginX || j == this.startMarginY || (k == this.startMarginZ || i == num1) ||
                            (j == num2 || k == num3))
                        {
                            float num4 = 1E+09f;
                            float num5 = 1E+09f;
                            float num6 = 1E+09f;
                            for (int index = 0; index < points.GetLength(0); ++index)
                            {
                                int point1 = points[index, 0];
                                int point2 = points[index, 1];
                                int point3 = points[index, 2];
                                int point4 = points[index, 3];
                                double val1_1 = (double) Math.Abs(i - point1);
                                double val1_2 = (double) Math.Abs(j - point2);
                                double val1_3 = (double) Math.Abs(k - point3);
                                double val2_1 = (double) this.wid - val1_1;
                                double val2_2 = (double) this.hei - val1_2;
                                double val2_3 = (double) this.zlvl - val1_3;
                                double num7 = Math.Min(val1_1, val2_1);
                                double num8 = Math.Min(val1_2, val2_2);
                                double num9 = Math.Min(val1_3, val2_3);
                                float num10 = (float) Math.Sqrt(num7 * num7 + num8 * num8 + num9 * num9);
                                if ((double) WaveAmp != 0.0)
                                    num10 += (float) Math.Sin((double) num10 * (double) WaveFactor) * WaveAmp;
                                if ((double) num10 <= (double) num4)
                                {
                                    num6 = num5;
                                    num5 = num4;
                                    num4 = num10;
                                }
                                else if ((double) num10 <= (double) num5)
                                {
                                    num6 = num5;
                                    num5 = num10;
                                }
                                else if ((double) num10 <= (double) num6)
                                    num6 = num10;
                            }
                            if ((double) num5 - (double) num4 + ((double) num6 - (double) num4) < 1.0)
                                this.setV(i, j, k, 0.0f);
                            else if ((double) num5 - (double) num4 + ((double) num6 - (double) num4) < (double) minDelta)
                                this.setV(i, j, k,
                                    (float)
                                    (((double) num5 - (double) num4 + ((double) num6 - (double) num4)) /
                                     (double) minDelta * (double) byte.MaxValue));
                            else
                                this.setV(i, j, k, (float) byte.MaxValue);
                        }
                    }
                }
            }
        }

        public void MandelbrotSet(double maxr, double minr, double maxi, double mini)
        {
            double num1 = (maxr - minr) / Convert.ToDouble(this.wid);
            double num2 = (maxi - mini) / Convert.ToDouble(this.hei);
            int num3 = 1000;
            for (int i = 0; i < this.wid; ++i)
            {
                double num4 = num1 * (double) i - Math.Abs(minr);
                for (int j = 0; j < this.hei; ++j)
                {
                    double num5 = 0.0;
                    double num6 = 0.0;
                    double num7 = num2 * (double) j - Math.Abs(mini);
                    int num8;
                    double num9;
                    for (num8 = 0; num5 * num5 + num6 * num6 <= 4.0 && num8 < num3; num6 = 2.0 * num9 * num6 + num7)
                    {
                        ++num8;
                        num9 = num5;
                        num5 = num5 * num5 - num6 * num6 + num4;
                    }
                    if (num8 != num3)
                        this.addV(i, j, (float) (num8 % (int) byte.MaxValue) * 2f);
                }
            }
        }

        public void crates(int N, int fromP, int toP, int fromR, int toR)
        {
            for (int index = 0; index < N; ++index)
                this.addCrate(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1), (double) this.r.Next(fromP, toP),
                    this.r.Next(fromR, toR));
        }

        private void addCrate(int x, int y, double power, int radius)
        {
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    double val1_1 = (double) Math.Abs(i - x);
                    double val1_2 = (double) Math.Abs(j - y);
                    double val2_1 = (double) this.wid - val1_1;
                    double val2_2 = (double) this.hei - val1_2;
                    double num =
                        Math.Sqrt(Math.Pow(Math.Min(val1_1, val2_1), 2.0) + Math.Pow(Math.Min(val1_2, val2_2), 2.0));
                    if (num < 0.001)
                        num = 0.001;
                    if (num < (double) radius)
                        num = 20.0;
                    if (this.n_dimensions == 3)
                    {
                        for (int k = 0; k < this.zlvl; ++k)
                            this.addV(i, j, k, (float) (power * (double) radius / num));
                    }
                    else
                        this.addV(i, j, (float) (power * (double) radius / num));
                }
            }
        }

        public void milkyay(int N, int fromP, int toP, int fromR, int toR)
        {
            for (int index = 0; index < N; ++index)
                this.addStar(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    (int) ((double) this.hei * this.nextGaussian(0.5, 0.08)), (double) this.r.Next(fromP, toP),
                    this.r.Next(fromR, toR));
        }

        public void gaussianstarfield(int N, int fromP, int toP, int fromR, int toR, double meanX, double varX,
            double meanY, double varY)
        {
            for (int index = 0; index < N; ++index)
                this.addStar((int) ((double) this.wid * this.nextGaussian(meanX, varX)),
                    (int) ((double) this.hei * this.nextGaussian(meanY, varY)), (double) this.r.Next(fromP, toP),
                    this.r.Next(fromR, toR));
        }

        public void starfield(int N, int fromP, int toP, int fromR, int toR)
        {
            for (int index = 0; index < N; ++index)
                this.addStar(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1), (double) this.r.Next(fromP, toP),
                    this.r.Next(fromR, toR));
        }

        private void addStarOLD(int x, int y, double power, int radius)
        {
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    double val1_1 = (double) Math.Abs(i - x);
                    double val1_2 = (double) Math.Abs(j - y);
                    double val2_1 = (double) this.wid - val1_1;
                    double val2_2 = (double) this.hei - val1_2;
                    double num =
                        Math.Sqrt(Math.Pow(Math.Min(val1_1, val2_1), 2.0) + Math.Pow(Math.Min(val1_2, val2_2), 2.0));
                    if (num < 0.001)
                        num = 0.001;
                    if (this.n_dimensions == 3)
                    {
                        for (int k = 0; k < this.zlvl; ++k)
                            this.addV(i, j, k, (float) (power * (double) radius / num));
                    }
                    else
                        this.addV(i, j, (float) (power * (double) radius / num));
                }
            }
        }

        private void addStar(int x, int y, double power, int radius)
        {
            int num1 = Math.Min(Math.Min(4 * (int) (power * (double) radius), this.wid / 2), this.hei / 2);
            for (int i1 = x - num1; i1 < x + num1; ++i1)
            {
                for (int i2 = y - num1; i2 < y + num1; ++i2)
                {
                    int i3 = this.seamlessCoord(i1, this.wid, this.seamlessX);
                    int j = this.seamlessCoord(i2, this.hei, this.seamlessY);
                    double val1_1 = (double) Math.Abs(i3 - x);
                    double val1_2 = (double) Math.Abs(j - y);
                    double val2_1 = (double) this.wid - val1_1;
                    double val2_2 = (double) this.hei - val1_2;
                    double num2 =
                        Math.Sqrt(Math.Pow(Math.Min(val1_1, val2_1), 2.0) + Math.Pow(Math.Min(val1_2, val2_2), 2.0));
                    if (num2 < 0.001)
                        num2 = 0.001;
                    if (this.n_dimensions == 3)
                    {
                        for (int k = 0; k < this.zlvl; ++k)
                            this.addV(i3, j, k, (float) (power * (double) radius / num2));
                    }
                    else
                        this.addV(i3, j, (float) (power * (double) radius / num2));
                }
            }
        }

        public void starfield3d(int N, int fromP, int toP, int fromR, int toR)
        {
            for (int index = 0; index < N; ++index)
                this.addStar3d(this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1),
                    this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1),
                    this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1), (double) this.r.Next(fromP, toP),
                    this.r.Next(fromR, toR));
        }

        public void cb_starfield(int N, int fromP, int toP, int fromR, int toR)
        {
            for (int index = 0; index < N; ++index)
            {
                int x = this.r.Next(this.startMarginX, this.wid - this.endMarginX - 1);
                int y = this.r.Next(this.startMarginY, this.hei - this.endMarginY - 1);
                int z = this.r.Next(this.startMarginZ, this.zlvl - this.endMarginZ - 1);
                switch (this.r.Next(0, 6))
                {
                    case 0:
                        z = this.startMarginZ;
                        break;
                    case 1:
                        z = this.zlvl - this.endMarginZ - 1;
                        break;
                    case 2:
                        x = this.startMarginX;
                        break;
                    case 3:
                        x = this.wid - this.endMarginX - 1;
                        break;
                    case 4:
                        y = this.startMarginY;
                        break;
                    case 5:
                        y = this.hei - this.endMarginY - 1;
                        break;
                }
                double power = (double) this.r.Next(fromP, toP);
                int radius = this.r.Next(fromR, toR);
                this.cb_addStar3d(x, y, z, power, radius);
            }
        }

        private void cb_addStar3d(int x, int y, int z, double power, int radius)
        {
            int num1 = this.wid - this.endMarginX - 1;
            int num2 = this.hei - this.endMarginY - 1;
            int num3 = this.zlvl - this.endMarginZ - 1;
            int num4 = Math.Min(Math.Min(Math.Min(4 * (int) (power * (double) radius), this.wid / 2), this.hei / 2),
                this.zlvl / 2);
            for (int i1 = x - num4; i1 < x + num4; ++i1)
            {
                for (int i2 = y - num4; i2 < y + num4; ++i2)
                {
                    for (int i3 = z - num4; i3 < z + num4; ++i3)
                    {
                        if (i1 == this.startMarginX || i2 == this.startMarginY ||
                            (i3 == this.startMarginZ || i1 == num1) || (i2 == num2 || i3 == num3))
                        {
                            int i4 = this.seamlessCoordKO(i1, this.wid, this.seamlessX);
                            int j = this.seamlessCoordKO(i2, this.hei, this.seamlessY);
                            int k = this.seamlessCoordKO(i3, this.zlvl, this.seamlessZ);
                            if (i4 >= 0 && j >= 0 && k >= 0)
                            {
                                double val1_1 = (double) Math.Abs(i4 - x);
                                double val1_2 = (double) Math.Abs(j - y);
                                double val1_3 = (double) Math.Abs(k - z);
                                double val2_1 = (double) this.wid - val1_1;
                                double val2_2 = (double) this.hei - val1_2;
                                double val2_3 = (double) this.zlvl - val1_3;
                                double num5 = Math.Min(val1_1, val2_1);
                                double num6 = Math.Min(val1_2, val2_2);
                                double num7 = Math.Min(val1_3, val2_3);
                                double num8 = Math.Sqrt(num5 * num5 + num6 * num6 + num7 * num7);
                                if (num8 < 0.001)
                                    num8 = 0.001;
                                if (this.n_dimensions == 3)
                                    this.addV(i4, j, k, (float) (power * (double) radius / num8));
                            }
                        }
                    }
                }
            }
        }

        private void addStar3d(int x, int y, int z, double power, int radius)
        {
            int num1 = Math.Min(Math.Min(Math.Min(4 * (int) (power * (double) radius), this.wid / 2), this.hei / 2),
                this.zlvl / 2);
            for (int i1 = x - num1; i1 < x + num1; ++i1)
            {
                for (int i2 = y - num1; i2 < y + num1; ++i2)
                {
                    for (int i3 = z - num1; i3 < z + num1; ++i3)
                    {
                        int i4 = this.seamlessCoordKO(i1, this.wid, this.seamlessX);
                        int j = this.seamlessCoordKO(i2, this.hei, this.seamlessY);
                        int k = this.seamlessCoordKO(i3, this.zlvl, this.seamlessZ);
                        if (i4 >= 0 && j >= 0 && k >= 0)
                        {
                            double val1_1 = (double) Math.Abs(i4 - x);
                            double val1_2 = (double) Math.Abs(j - y);
                            double val1_3 = (double) Math.Abs(k - z);
                            double val2_1 = (double) this.wid - val1_1;
                            double val2_2 = (double) this.hei - val1_2;
                            double val2_3 = (double) this.zlvl - val1_3;
                            double num2 = Math.Min(val1_1, val2_1);
                            double num3 = Math.Min(val1_2, val2_2);
                            double num4 = Math.Min(val1_3, val2_3);
                            double num5 = Math.Sqrt(num2 * num2 + num3 * num3 + num4 * num4);
                            if (num5 < 0.001)
                                num5 = 0.001;
                            if (this.n_dimensions == 3)
                                this.addV(i4, j, k, (float) (power * (double) radius / num5));
                        }
                    }
                }
            }
        }

        public void UseFuncPipe()
        {
            this.useFuncPipe = true;
        }

        private void addFunc(cnt_f1D f)
        {
            this.fPipe.Add(f);
        }

        public void CommitFuncPipe()
        {
            if (this.fPipe.Count <= 0)
                return;
            if (this.n_dimensions == 2)
            {
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                    {
                        float num = this.getV(i, j);
                        foreach (cnt_f1D cntF1D in this.fPipe)
                            num = cntF1D(num);
                        this.setV(i, j, num);
                    }
                }
                this.fPipe.Clear();
            }
            else if (this.n_dimensions == 3)
            {
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                    {
                        for (int k = 0; k < this.zlvl; ++k)
                        {
                            float num = this.getV(i, j, k);
                            foreach (cnt_f1D cntF1D in this.fPipe)
                                num = cntF1D(num);
                            this.setV(i, j, k, num);
                        }
                    }
                }
                this.fPipe.Clear();
            }
            else
            {
                if (this.n_dimensions != 1)
                    return;
                for (int index = 0; index < this.wid; ++index)
                {
                    float num = this.getV((long) index);
                    foreach (cnt_f1D cntF1D in this.fPipe)
                        num = cntF1D(num);
                    this.setV((long) index, num);
                }
                this.fPipe.Clear();
            }
        }

        public void CubeMap()
        {
            int wid = this.wid;
            int xDim = wid * 3;
            int yDim = wid * 3;
            float[,] numArray = new float[xDim, yDim];
            if (this.n_dimensions == 3)
            {
                for (int i = 0; i < wid; ++i)
                {
                    for (int j = 0; j < wid; ++j)
                        numArray[wid + i, wid + j] = this.getV(i, j, 0);
                }
                for (int i = 0; i < wid; ++i)
                {
                    for (int j = 0; j < wid; ++j)
                        numArray[wid * 3 - 1 - i, wid * 2 + j] = this.getV(i, j, wid - 1);
                }
                for (int k = 0; k < wid; ++k)
                {
                    for (int j = 0; j < wid; ++j)
                        numArray[wid * 2 + k, wid + j] = this.getV(wid - 1, j, k);
                }
                for (int k = 0; k < wid; ++k)
                {
                    for (int j = 0; j < wid; ++j)
                        numArray[wid - 1 - k, wid + j] = this.getV(0, j, k);
                }
                for (int i = 0; i < wid; ++i)
                {
                    for (int k = 0; k < wid; ++k)
                        numArray[wid + i, wid - 1 - k] = this.getV(i, 0, k);
                }
                for (int i = 0; i < wid; ++i)
                {
                    for (int k = 0; k < wid; ++k)
                        numArray[wid + i, wid * 2 + k] = this.getV(i, wid - 1, k);
                }
            }
            this.set2D(xDim, yDim);
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                    this.setV(i, j, numArray[i, j]);
            }
        }

        public void TESTmakeSphereMap()
        {
            float[,] numArray = new float[this.wid, this.hei];
            double wid = (double) this.wid;
            double hei = (double) this.hei;
            for (int i = 0; i < this.wid; ++i)
            {
                for (int index = 0; index < this.hei; ++index)
                {
                    double num1 = Math.Asin(2.0 * ((double) i / wid) - 1.0) / Math.PI;
                    double num2 = (Math.Asin(2.0 * ((double) index / hei) - 1.0) / Math.PI + 0.5) * hei;
                    try
                    {
                        numArray[i, index] = this.getV(i, (int) num2);
                    }
                    catch
                    {
                    }
                }
            }
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                    this.setV(i, j, numArray[i, j]);
            }
        }

        public void Wall(int lines, int cols, int LineShift, int size, int pow, float rndmColW01)
        {
            int num1 = this.hei / lines;
            for (int index1 = 0; index1 < lines; ++index1)
            {
                int num2 = index1 * num1;
                for (int index2 = 0; index2 < size; ++index2)
                {
                    for (int i1 = 0; i1 < this.wid; ++i1)
                    {
                        int i2 = this.seamlessCoord(i1, this.wid, this.seamlessX);
                        int j1 = this.seamlessCoord(num2 + index2, this.hei, this.seamlessY);
                        this.setV(i2, j1, (float) (index2 * pow));
                        int j2 = this.seamlessCoord(num2 - index2, this.hei, this.seamlessY);
                        this.setV(i2, j2, (float) (index2 * pow));
                    }
                }
            }
            int num3 = this.wid / cols;
            int minValue = 0;
            if ((double) rndmColW01 < 0.0)
            {
                minValue = (int) ((double) this.clamp(rndmColW01, 0.0f, 1f) * (double) num3);
                rndmColW01 *= -1f;
            }
            int maxValue = (int) ((double) this.clamp(rndmColW01, 0.0f, 1f) * (double) num3);
            for (int index1 = 0; index1 < lines; ++index1)
            {
                int num2 = index1 * num1;
                for (int index2 = 0; index2 < cols; ++index2)
                {
                    int num4 = this.r.Next(minValue, maxValue);
                    int num5 = index2 * num3 + num4 + index1 * LineShift;
                    for (int index3 = 0; index3 < size; ++index3)
                    {
                        for (int index4 = 0; index4 < num1; ++index4)
                        {
                            if (index4 < num1 - index3 && index4 > index3)
                            {
                                int j = this.seamlessCoord(num2 + index4, this.hei, this.seamlessY);
                                this.setV(this.seamlessCoord(num5 + index3, this.wid, this.seamlessX), j,
                                    (float) (index3 * pow));
                                this.setV(this.seamlessCoord(num5 - index3, this.wid, this.seamlessX), j,
                                    (float) (index3 * pow));
                            }
                        }
                    }
                }
            }
        }

        public void Tile(int size, float pow)
        {
            if (size > this.wid)
                size = this.wid;
            if (size > this.hei)
                size = this.hei;
            if (this.n_dimensions == 2)
            {
                for (int index = 0; index < this.wid; ++index)
                {
                    for (int j = 0; j < size; ++j)
                    {
                        if (index < this.wid - j * 2)
                        {
                            this.setV(index + j, j, (float) j * pow);
                            this.setV(index + j, this.hei - 1 - j, (float) j * pow);
                        }
                    }
                }
                for (int index = 0; index < this.hei; ++index)
                {
                    for (int i = 0; i < size; ++i)
                    {
                        if (index < this.hei - i * 2)
                        {
                            this.setV(i, index + i, (float) i * pow);
                            this.setV(this.wid - 1 - i, index + i, (float) i * pow);
                        }
                    }
                }
            }
            if (this.n_dimensions != 3 || !this.isCB)
                return;
            int num = this.wid - 1;
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < size; ++index2)
                {
                    if (index1 < this.wid - index2 * 2)
                    {
                        this.setV(index1 + index2, index2, 0, (float) index2 * pow);
                        this.setV(index1 + index2, num - index2, 0, (float) index2 * pow);
                        this.setV(index2, index1 + index2, 0, (float) index2 * pow);
                        this.setV(num - index2, index1 + index2, 0, (float) index2 * pow);
                        this.setV(index1 + index2, index2, num, (float) index2 * pow);
                        this.setV(index1 + index2, num - index2, this.zlvl - 1, (float) index2 * pow);
                        this.setV(index2, index1 + index2, num, (float) index2 * pow);
                        this.setV(num - index2, index1 + index2, num, (float) index2 * pow);
                        this.setV(index1 + index2, num, index2, (float) index2 * pow);
                        this.setV(index1 + index2, num, num - index2, (float) index2 * pow);
                        this.setV(index2, num, index1 + index2, (float) index2 * pow);
                        this.setV(num - index2, num, index1 + index2, (float) index2 * pow);
                        this.setV(index1 + index2, 0, index2, (float) index2 * pow);
                        this.setV(index1 + index2, 0, num - index2, (float) index2 * pow);
                        this.setV(index2, 0, index1 + index2, (float) index2 * pow);
                        this.setV(num - index2, 0, index1 + index2, (float) index2 * pow);
                        this.setV(0, index2, index1 + index2, (float) index2 * pow);
                        this.setV(0, num - index2, index1 + index2, (float) index2 * pow);
                        this.setV(0, index1 + index2, index2, (float) index2 * pow);
                        this.setV(0, index1 + index2, num - index2, (float) index2 * pow);
                        this.setV(num, index2, index1 + index2, (float) index2 * pow);
                        this.setV(num, this.hei - 1 - index2, index1 + index2, (float) index2 * pow);
                        this.setV(num, index1 + index2, index2, (float) index2 * pow);
                        this.setV(num, index1 + index2, this.hei - 1 - index2, (float) index2 * pow);
                    }
                }
            }
        }

        public void Scale(float minV, float maxV)
        {
            if (this.n_dimensions == 2)
            {
                float num1 = 1E+08f;
                float num2 = -1E+08f;
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                    {
                        float v = this.getV(i, j);
                        if ((double) v > (double) num2)
                            num2 = v;
                        if ((double) v < (double) num1)
                            num1 = v;
                    }
                }
                if ((double) num2 - (double) num1 == 0.0)
                    return;
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                    {
                        float v = this.getV(i, j);
                        this.setV(i, j,
                            minV +
                            (float)
                            (((double) v - (double) num1) / ((double) num2 - (double) num1) *
                             ((double) maxV - (double) minV)));
                    }
                }
            }
            else if (this.n_dimensions == 3)
            {
                float num1 = 1E+08f;
                float num2 = -1E+08f;
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                    {
                        for (int k = 0; k < this.zlvl; ++k)
                        {
                            float v = this.getV(i, j, k);
                            if ((double) v > (double) num2)
                                num2 = v;
                            if ((double) v < (double) num1)
                                num1 = v;
                        }
                    }
                }
                if ((double) num2 - (double) num1 == 0.0)
                    return;
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                    {
                        for (int k = 0; k < this.zlvl; ++k)
                        {
                            float v = this.getV(i, j, k);
                            this.setV(i, j, k,
                                minV +
                                (float)
                                (((double) v - (double) num1) / ((double) num2 - (double) num1) *
                                 ((double) maxV - (double) minV)));
                        }
                    }
                }
            }
            else
            {
                if (this.n_dimensions != 1)
                    return;
                float num1 = 1E+08f;
                float num2 = -1E+08f;
                for (int index = 0; index < this.wid; ++index)
                {
                    if ((double) this.getV((long) index) > (double) num2)
                        num2 = this.getV((long) index);
                    if ((double) this.getV((long) index) < (double) num1)
                        num1 = this.getV((long) index);
                }
                if ((double) num2 - (double) num1 == 0.0)
                    return;
                for (int index = 0; index < this.wid; ++index)
                    this.setV((long) index,
                        minV +
                        (float)
                        (((double) this.getV((long) index) - (double) num1) / ((double) num2 - (double) num1) *
                         ((double) maxV - (double) minV)));
            }
        }

        public void Wave(float wf, float amp)
        {
            cnt_f1D f = (cnt_f1D) (x => x + (float) Math.Sin((double) x * (double) wf) * amp);
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void Cartoon(int V)
        {
            if (V == 0)
                V = 1;
            cnt_f1D f = (cnt_f1D) (x => x - x % (float) V);
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void SetRange(float fromV, float toV, float newV)
        {
            cnt_f1D f = (cnt_f1D) (x =>
            {
                if ((double) x > (double) toV || (double) x < (double) fromV)
                    return x;
                return newV;
            });
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void pixelNoise(double delta, int positive)
        {
            if (positive == 0)
            {
                cnt_f1D f = (cnt_f1D) (x => x + (float) (this.r.NextDouble() * (2.0 * delta) - delta));
                if (this.useFuncPipe)
                    this.addFunc(f);
                else
                    this.Func(f);
            }
            else
            {
                cnt_f1D f = (cnt_f1D) (x => x + (float) (this.r.NextDouble() * delta));
                if (this.useFuncPipe)
                    this.addFunc(f);
                else
                    this.Func(f);
            }
        }

        public void pixelRangeNoise(double delta, int positive, float fromV, float toV)
        {
            if (positive == 0)
            {
                cnt_f1D f = (cnt_f1D) (x =>
                {
                    if ((double) x > (double) toV || (double) x < (double) fromV)
                        return x;
                    return x + (float) (this.r.NextDouble() * (2.0 * delta) - delta);
                });
                if (this.useFuncPipe)
                    this.addFunc(f);
                else
                    this.Func(f);
            }
            else
            {
                cnt_f1D f = (cnt_f1D) (x =>
                {
                    if ((double) x > (double) toV || (double) x < (double) fromV)
                        return x;
                    return x + (float) (this.r.NextDouble() * delta);
                });
                if (this.useFuncPipe)
                    this.addFunc(f);
                else
                    this.Func(f);
            }
        }

        public void Mod(float V)
        {
            cnt_f1D f = (cnt_f1D) (x => x % V);
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void And(float V)
        {
            cnt_f1D f = (cnt_f1D) (x => (float) ((int) x & (int) V));
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void Cut(float CutValue)
        {
            cnt_f1D f = (cnt_f1D) (x =>
            {
                if ((double) x >= (double) CutValue)
                    return CutValue;
                return x;
            });
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void Invert()
        {
            cnt_f1D f = (cnt_f1D) (x => (float) byte.MaxValue - x);
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void FlatLog(float level)
        {
            cnt_f1D f =
                (cnt_f1D) (x => x - (float) Math.Log(1.0 + (double) Math.Abs(x - level)) * (float) Math.Sign(x - level));
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void addContrast(float Contrast)
        {
            byte[] contrast_lookup = new byte[256];
            double num1 = (100.0 + (double) Contrast) / 100.0;
            double num2 = num1 * num1;
            for (int index = 0; index < 256; ++index)
            {
                double num3 = (((double) index / (double) byte.MaxValue - 0.5) * num2 + 0.5) * (double) byte.MaxValue;
                if (num3 < 0.0)
                    num3 = 0.0;
                if (num3 > (double) byte.MaxValue)
                    num3 = (double) byte.MaxValue;
                contrast_lookup[index] = (byte) num3;
            }
            cnt_f1D f = (cnt_f1D) (x =>
            {
                if ((double) x >= (double) byte.MaxValue)
                    return (float) contrast_lookup[(int) byte.MaxValue];
                if ((double) x <= 0.0)
                    return (float) contrast_lookup[0];
                return (float) contrast_lookup[(int) x];
            });
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void Flat(float level, float smoth)
        {
            if ((double) smoth > 1.0)
                smoth = 1f;
            if ((double) smoth < 0.0)
                smoth = 0.0f;
            cnt_f1D f = (cnt_f1D) (x => x - (x - level) * smoth);
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void MulV(float V)
        {
            cnt_f1D f = (cnt_f1D) (x => x * V);
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void Landscape(float seaLevel)
        {
            cnt_f1D f = (cnt_f1D) (x =>
            {
                if ((double) x >= (double) seaLevel)
                    return x - seaLevel;
                return 0.0f;
            });
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void Enance(float fromV, float toV)
        {
            cnt_f1D f = (cnt_f1D) (x =>
            {
                if ((double) x >= (double) fromV && (double) x <= (double) toV)
                    return x;
                return 0.0f;
            });
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void EnanceB(float fromV, float toV)
        {
            cnt_f1D f =
                (cnt_f1D)
                (x => (double) x < (double) fromV ? 0.0f : ((double) x > (double) toV ? 0.0f : (float) byte.MaxValue));
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void EnanceBorder(float fromV, float toV)
        {
            cnt_f1D f = (cnt_f1D) (x =>
            {
                if ((double) x < (double) fromV)
                    return x * 0.7f;
                if ((double) x <= (double) toV)
                    return toV + x * 0.7f;
                return 0.0f;
            });
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void EnanceS(float fromV, float toV)
        {
            cnt_f1D f = (cnt_f1D) (x =>
            {
                if ((double) x < (double) fromV)
                    return x * 0.5f;
                if ((double) x <= (double) toV)
                    return x;
                return toV + (float) (((double) x - (double) toV) * 0.5);
            });
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void EnanceS1(float fromV, float toV)
        {
            cnt_f1D f = (cnt_f1D) (x =>
            {
                if ((double) x >= (double) fromV && (double) x <= (double) toV)
                    return x;
                return x * 0.5f;
            });
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void EnanceS2(float fromV, float toV)
        {
            cnt_f1D f = (cnt_f1D) (x =>
            {
                if ((double) x < (double) fromV)
                    return x * 0.5f;
                if ((double) x <= (double) toV)
                    return x;
                return (float) ((double) byte.MaxValue - (double) x * 0.5);
            });
            if (this.useFuncPipe)
                this.addFunc(f);
            else
                this.Func(f);
        }

        public void Smooth()
        {
            if (this.n_dimensions == 2)
            {
                Gen cntNoise = new Gen(this.wid, this.hei, this.Seed);
                for (int i1 = 0; i1 < this.wid; ++i1)
                {
                    for (int j1 = 0; j1 < this.hei; ++j1)
                    {
                        int i2 = (i1 - 1) % this.wid;
                        if (i2 < 0)
                            i2 = this.wid + i2;
                        int i3 = (i1 + 1) % this.wid;
                        if (i3 < 0)
                            i3 = this.wid + i3;
                        int j2 = (j1 - 1) % this.hei;
                        if (j2 < 0)
                            j2 = this.hei + j2;
                        int j3 = (j1 + 1) % this.hei;
                        if (j3 < 0)
                            j3 = this.hei + j3;
                        cntNoise.setPix(i1, j1,
                            (float)
                            (((double) this.getV(i2, j2) + (double) this.getV(i2, j3) + (double) this.getV(i3, j3) +
                              (double) this.getV(i3, j2)) / 16.0 +
                             ((double) this.getV(i2, j1) + (double) this.getV(i1, j3) + (double) this.getV(i3, j1) +
                              (double) this.getV(i1, j2)) / 8.0 + (double) this.getV(i1, j1) / 4.0));
                    }
                }
                this._Values1D = cntNoise.getValues1D();
            }
            if (this.n_dimensions == 3)
            {
                Gen cntNoise = new Gen(this.wid, this.hei, this.zlvl, this.Seed);
                for (int i1 = 0; i1 < this.wid; ++i1)
                {
                    for (int j1 = 0; j1 < this.hei; ++j1)
                    {
                        for (int k = 0; k < this.zlvl; ++k)
                        {
                            int i2 = (i1 - 1) % this.wid;
                            if (i2 < 0)
                                i2 = this.wid + i2;
                            int i3 = (i1 + 1) % this.wid;
                            if (i3 < 0)
                                i3 = this.wid + i3;
                            int j2 = (j1 - 1) % this.hei;
                            if (j2 < 0)
                                j2 = this.hei + j2;
                            int j3 = (j1 + 1) % this.hei;
                            if (j3 < 0)
                                j3 = this.hei + j3;
                            cntNoise.setPix(i1, j1, k,
                                (float)
                                (((double) this.getV(i2, j2, k) + (double) this.getV(i2, j3, k) +
                                  (double) this.getV(i3, j3, k) + (double) this.getV(i3, j2, k)) / 16.0 +
                                 ((double) this.getV(i2, j1, k) + (double) this.getV(i1, j3, k) +
                                  (double) this.getV(i3, j1, k) + (double) this.getV(i1, j2, k)) / 8.0 +
                                 (double) this.getV(i1, j1, k) / 4.0));
                        }
                    }
                }
                this._Values1D = cntNoise.getValues1D();
            }
            if (this.n_dimensions != 1)
                return;
            Gen cntNoise1 = new Gen(this.wid, this.Seed);
            for (int i = 0; i < this.wid; ++i)
            {
                int num1 = (i - 1) % this.wid;
                if (num1 < 0)
                    num1 = this.wid + num1;
                int num2 = (i + 1) % this.wid;
                if (num2 < 0)
                    num2 = this.wid + num2;
                cntNoise1.setPix(i,
                    (float)
                    (((double) this.getV((long) num1) + (double) this.getV((long) num2)) / 4.0 +
                     (double) this.getV((long) i) / 2.0));
            }
            this._Values1D = cntNoise1.getValues1D();
        }

        public void Func(cnt_f1D f)
        {
            if (this.n_dimensions == 2)
            {
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                        this.setV(i, j, f(this.getV(i, j)));
                }
            }
            else if (this.n_dimensions == 3)
            {
                if (!this.isCB)
                {
                    for (int i = 0; i < this.wid; ++i)
                    {
                        for (int j = 0; j < this.hei; ++j)
                        {
                            for (int k = 0; k < this.zlvl; ++k)
                                this.setV(i, j, k, f(this.getV(i, j, k)));
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < this.wid; ++i)
                    {
                        for (int j = 0; j < this.hei; ++j)
                        {
                            for (int k = 0; k < this.zlvl; ++k)
                            {
                                if (i == 0 || j == 0 || (k == 0 || i == this.wid - 1) ||
                                    (j == this.hei - 1 || k == this.zlvl - 1))
                                    this.setV(i, j, k, f(this.getV(i, j, k)));
                            }
                        }
                    }
                }
            }
            else
            {
                if (this.n_dimensions != 1)
                    return;
                for (int index = 0; index < this.wid; ++index)
                    this.setV((long) index, f(this.getV((long) index)));
            }
        }

        private void NoiseNear(float V)
        {
            if (this.n_dimensions != 2)
                return;
            for (int i1 = 0; i1 < this.wid; ++i1)
            {
                for (int j1 = 0; j1 < this.hei; ++j1)
                {
                    int i2 = this.seamlessCoord(i1 - 1, this.wid, this.seamlessX);
                    int i3 = this.seamlessCoord(i1 + 1, this.wid, this.seamlessX);
                    int j2 = this.seamlessCoord(j1 - 1, this.hei, this.seamlessY);
                    int j3 = this.seamlessCoord(j1 + 1, this.hei, this.seamlessY);
                    float v1 = this.getV(i1, j1);
                    float v2 = this.getV(i1, j2);
                    float v3 = this.getV(i1, j3);
                    float v4 = this.getV(i2, j1);
                    float v5 = this.getV(i3, j1);
                    float num1 = 20f;
                    int num2 = 0;
                    if ((double) Math.Abs(v2 - V) < (double) num1)
                        ++num2;
                    if ((double) Math.Abs(v3 - V) < (double) num1)
                        ++num2;
                    if ((double) Math.Abs(v4 - V) < (double) num1)
                        ++num2;
                    if ((double) Math.Abs(v5 - V) < (double) num1)
                        ++num2;
                    if ((double) v1 != (double) V && num2 > 0 && this.r.Next(0, 3 + num2) <= 1)
                        this.setV(i1, j1, V);
                }
            }
        }

        public void Func(cnt_fSpatial3D f)
        {
            if (this.n_dimensions == 2)
            {
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                        this.setV(i, j, f(this.getV(i, j), i, j, 0));
                }
            }
            else if (this.n_dimensions == 3)
            {
                if (!this.isCB)
                {
                    for (int i = 0; i < this.wid; ++i)
                    {
                        for (int j = 0; j < this.hei; ++j)
                        {
                            for (int k = 0; k < this.zlvl; ++k)
                                this.setV(i, j, k, f(this.getV(i, j, k), i, j, k));
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < this.wid; ++i)
                    {
                        for (int j = 0; j < this.hei; ++j)
                        {
                            for (int k = 0; k < this.zlvl; ++k)
                            {
                                if (i == 0 || j == 0 || (k == 0 || i == this.wid - 1) ||
                                    (j == this.hei - 1 || k == this.zlvl - 1))
                                    this.setV(i, j, k, f(this.getV(i, j, k), i, j, k));
                            }
                        }
                    }
                }
            }
            else
            {
                if (this.n_dimensions != 1)
                    return;
                for (int i = 0; i < this.wid; ++i)
                    this.setV((long) i, f(this.getV((long) i), i, 0, 0));
            }
        }

        public void Marble(float xPeriod, float yPeriod, float zPeriod, float Power, float Size)
        {
            double xfactor = 0.0;
            if (this.wid > 0)
                xfactor = (double) xPeriod / (double) this.wid;
            double yfactor = 0.0;
            if (this.hei > 0)
                yfactor = (double) yPeriod / (double) this.hei;
            double zfactor = 0.0;
            if (this.zlvl > 0)
                zfactor = (double) zPeriod / (double) this.zlvl;
            double pow = (double) Power / 256.0;
            this.Func(
                (cnt_fSpatial3D)
                ((v, i, j, k) =>
                    (float)
                    Math.Abs(
                        Math.Sin(((double) i * xfactor + (double) j * yfactor + (double) k * zfactor + (double) v * pow) *
                                 Math.PI)) * 256f));
        }

        public void MORPH(Gen Noise2dFrom, Gen Noise2dTo, int frames, int cos0lin1)
        {
            if (Noise2dFrom.getZlvl() > 0 || Noise2dTo.getZlvl() > 0)
                return;
            int xDim = Math.Min(Noise2dFrom.getWid(), Noise2dTo.getWid());
            int yDim = Math.Min(Noise2dFrom.getHei(), Noise2dTo.getHei());
            int zDim = frames;
            int k1 = zDim / 2;
            this.set3D(xDim, yDim, zDim);
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    for (int k2 = 0; k2 < k1; ++k2)
                    {
                        if (cos0lin1 == 0)
                            this.setV(i, j, k2,
                                this.cos_Interpolate(Noise2dFrom.getV(i, j), Noise2dTo.getV(i, j),
                                    (float) k2 / (float) k1));
                        else
                            this.setV(i, j, k2,
                                this.lin_Interpolate(Noise2dFrom.getV(i, j), Noise2dTo.getV(i, j),
                                    (float) k2 / (float) k1));
                    }
                    this.setV(i, j, k1, Noise2dTo.getV(i, j));
                    for (int k2 = k1 + 1; k2 < zDim; ++k2)
                    {
                        if (cos0lin1 == 0)
                            this.setV(i, j, k2,
                                this.cos_Interpolate(Noise2dTo.getV(i, j), Noise2dFrom.getV(i, j),
                                    (float) (k2 - k1) / (float) k1));
                        else
                            this.setV(i, j, k2,
                                this.lin_Interpolate(Noise2dTo.getV(i, j), Noise2dFrom.getV(i, j),
                                    (float) (k2 - k1) / (float) k1));
                    }
                }
            }
        }

        public void Func(cnt_f f, Gen A)
        {
            if (this.n_dimensions == 2)
            {
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                        this.setV(i, j, f(this.getV(i, j), A.getPix(i, j)));
                }
            }
            else if (this.n_dimensions == 3)
            {
                if (!this.isCB)
                {
                    for (int i = 0; i < this.wid; ++i)
                    {
                        for (int j = 0; j < this.hei; ++j)
                        {
                            for (int k = 0; k < this.zlvl; ++k)
                                this.setV(i, j, k, f(this.getV(i, j, k), A.getPix(i, j, k)));
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < this.wid; ++i)
                    {
                        for (int j = 0; j < this.hei; ++j)
                        {
                            for (int k = 0; k < this.zlvl; ++k)
                            {
                                if (i == 0 || j == 0 || (k == 0 || i == this.wid - 1) ||
                                    (j == this.hei - 1 || k == this.zlvl - 1))
                                    this.setV(i, j, k, f(this.getV(i, j, k), A.getPix(i, j, k)));
                            }
                        }
                    }
                }
            }
            else
            {
                if (this.n_dimensions != 1)
                    return;
                for (int i = 0; i < this.wid; ++i)
                    this.setV((long) i, f(this.getV((long) i), A.getPix(i)));
            }
        }

        public void Add(Gen A)
        {
            this.Func((cnt_f) ((x, y) => x + y), A);
        }

        public void Sculpt(Gen A, float intensityMin1Plus1, float fromV, float toV)
        {
            this.Func((cnt_f) ((x, y) =>
            {
                if ((double) x < (double) fromV || (double) x > (double) toV)
                    return x;
                return x + intensityMin1Plus1 * y;
            }), A);
        }

        public void ReplaceRange(float fromV, float toV, Gen Noise)
        {
            this.Func((cnt_f) ((x, y) =>
            {
                if ((double) x < (double) fromV || (double) x > (double) toV)
                    return x;
                return y;
            }), Noise);
        }

        public void MixRange(float fromV, float toV, Gen Noise)
        {
            this.Func((cnt_f) ((x, y) =>
            {
                if ((double) x < (double) fromV || (double) x > (double) toV)
                    return x;
                return (float) (((double) x + (double) y) / 2.0);
            }), Noise);
        }

        public void AddRange(float fromV, float toV, Gen Noise)
        {
            this.Func((cnt_f) ((x, y) =>
            {
                if ((double) x < (double) fromV || (double) x > (double) toV)
                    return x;
                return x + y;
            }), Noise);
        }

        public void Mul(Gen A)
        {
            this.Func((cnt_f) ((x, y) => x * y), A);
        }

        public void Subtract(Gen A)
        {
            this.Func((cnt_f) ((x, y) => x - y), A);
        }

        public void Inter(Gen A)
        {
            this.Func((cnt_f) ((x, y) => x * (y / (float) byte.MaxValue)), A);
        }

        public void BitAnd(Gen A)
        {
            this.Func((cnt_f) ((x, y) => (float) ((int) x & (int) y)), A);
        }

        public void BitOr(Gen A)
        {
            this.Func((cnt_f) ((x, y) => (float) ((int) x | (int) y)), A);
        }

        public void BitXor(Gen A)
        {
            this.Func((cnt_f) ((x, y) => (float) ((int) x ^ (int) y)), A);
        }

        public void Log(Gen A)
        {
            this.Func((cnt_f) ((x, y) => x * (float) Math.Log((double) y + 1.0)), A);
        }

        public void Sin(Gen A)
        {
            this.Func((cnt_f) ((x, y) => x + (float) Math.Sin((double) y)), A);
        }

        public void Mix(Gen A)
        {
            this.Func((cnt_f) ((x, y) => (float) (((double) x + (double) y) / 2.0)), A);
        }

        public void Bal(Gen A)
        {
            this.Func((cnt_f) ((x, y) => (float) ((double) x * (double) x / (1.0 + (double) y))), A);
        }

        public void reNoise(int n_rep, int b_size, float red_fac, int deep, int delta)
        {
            Gen cntNoise = new Gen(256, this.r.Next());
            cntNoise.F_Noise1D(n_rep, b_size, (double) red_fac, deep, delta);
            if (this.n_dimensions == 2)
            {
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                        this.setV(i, j, cntNoise.getPix((int) this.getV(i, j)));
                }
            }
            else if (this.n_dimensions == 3)
            {
                for (int i = 0; i < this.wid; ++i)
                {
                    for (int j = 0; j < this.hei; ++j)
                    {
                        for (int k = 0; k < this.zlvl; ++k)
                            this.setV(i, j, k, cntNoise.getPix((int) this.getV(i, j, k)));
                    }
                }
            }
            else
            {
                if (this.n_dimensions != 1)
                    return;
                for (int index = 0; index < this.wid; ++index)
                    this.setV((long) index, cntNoise.getPix((int) this.getV((long) index)));
            }
        }

        private void FuncTile(int startX, int startY, int startZ, cnt_f f, Gen A)
        {
            if (this.n_dimensions != 2)
                return;
            for (int i1 = 0; i1 < A.wid; ++i1)
            {
                for (int j1 = 0; j1 < A.hei; ++j1)
                {
                    int i2 = this.seamlessCoord(startX + i1, this.wid, this.seamlessX);
                    int j2 = this.seamlessCoord(startY + j1, this.hei, this.seamlessY);
                    this.setV(i2, j2, f(this.getV(i2, j2), A.getPix(i1, j1)));
                }
            }
        }

        private void FuncTiles(int startX, int startY, int startZ, cnt_f f, Gen A)
        {
            int startY1 = startY;
            while (startY1 < this.hei)
            {
                int startX1 = startX;
                while (startX1 < this.wid)
                {
                    this.FuncTile(startX1, startY1, startZ, f, A);
                    startX1 += A.wid;
                }
                startY1 += A.hei;
            }
        }

        public void AddTiles(int startX, int startY, int startZ, Gen A)
        {
            cnt_f f = (cnt_f) ((x, y) => x + y);
            this.FuncTiles(startX, startY, startZ, f, A);
        }

        private int seamlessCoord(int i, int wid, bool isSeamless)
        {
            if (!isSeamless)
                return this.clamp(i, 0, wid - 1);
            int num = i % wid;
            if (num < 0) num = wid + num;
            return num;
        }

        private int seamlessCoordKO(int i, int wid, bool isSeamless)
        {
            if (isSeamless)
            {
                int num = i % wid;
                if (num < 0)
                    num = wid + num;
                return num;
            }
            if (i > wid - 1)
                return -1;
            if (i < 0)
                return -2;
            return i;
        }

        private int seamlessCoord(int x, int i, int wid, bool isSeamless)
        {
            if (!isSeamless)
                return x + i;
            int num = (x + i) % wid;
            if (num < 0)
                num = wid + num;
            return num;
        }

        private bool coordisvalid(int i)
        {
            return i < this.wid & i >= 0;
        }

        private bool coordisvalid(int i, int j)
        {
            return i < this.wid & i >= 0 & j < this.hei & j >= 0;
        }

        private bool coordisvalid(int i, int j, int k)
        {
            return i < this.wid & i >= 0 & j < this.hei & j >= 0 & k < this.zlvl & k >= 0;
        }

        public void setPix(int i, float v)
        {
            this.setV((long) (i % this.wid), v);
        }

        public void setPix(int i, int j, float v)
        {
            this.setV(i % this.wid, j % this.hei, v);
        }

        public void setPix(int i, int j, int k, float v)
        {
            this.setV(i % this.wid, j % this.hei, k % this.zlvl, v);
        }

        public float getPix(int i)
        {
            int num = i % this.wid;
            if (num < 0)
                num = this.wid + num;
            return this.getV((long) num);
        }

        public float getPix(int i, int j)
        {
            int i1 = i % this.wid;
            if (i1 < 0)
                i1 = this.wid + i1;
            int j1 = j % this.hei;
            if (j1 < 0)
                j1 = this.hei + j1;
            return this.getV(i1, j1);
        }

        private float getPix01(int i, int j)
        {
            int i1 = i % this.wid;
            if (i1 < 0)
                i1 = this.wid + i1;
            int j1 = j % this.hei;
            if (j1 < 0)
                j1 = this.hei + j1;
            float num = this.getV(i1, j1);
            if ((double) num < 0.0)
                num = 0.0f;
            if ((double) num > (double) byte.MaxValue)
                num = (float) byte.MaxValue;
            return num / (float) byte.MaxValue;
        }

        public float getPix(int i, int j, int k)
        {
            int i1 = i % this.wid;
            if (i1 < 0)
                i1 = this.wid + i1;
            int j1 = j % this.hei;
            if (j1 < 0)
                j1 = this.hei + j1;
            int k1 = k % this.zlvl;
            if (k1 < 0)
                k1 = this.zlvl + k1;
            return this.getV(i1, j1, k1);
        }

        private float getPix01(int i, int j, int k)
        {
            int i1 = i % this.wid;
            if (i1 < 0)
                i1 = this.wid + i1;
            int j1 = j % this.hei;
            if (j1 < 0)
                j1 = this.hei + j1;
            int k1 = k % this.zlvl;
            if (k1 < 0)
                k1 = this.zlvl + k1;
            float num = this.getV(i1, j1, k1);
            if ((double) num < 0.0)
                num = 0.0f;
            if ((double) num > (double) byte.MaxValue)
                num = (float) byte.MaxValue;
            return num / (float) byte.MaxValue;
        }

        private float getPix01NoSeam(int i, int j, int k)
        {
            float num = this.getV(this.clamp(i, 0, this.wid - 1), this.clamp(j, 0, this.hei - 1),
                this.clamp(k, 0, this.zlvl - 1));
            if ((double) num < 0.0)
                num = 0.0f;
            if ((double) num > (double) byte.MaxValue)
                num = (float) byte.MaxValue;
            return num / (float) byte.MaxValue;
        }

        public void setLatfactor(float v)
        {
            this.Lat01Factor = v;
        }

        public void RandomColFrom(int red, int green, int blue, int N)
        {
            int red1 = red;
            int green1 = green;
            int blue1 = blue;
            for (int index = 0; index < N; ++index)
            {
                this.ColorsGradient.Add(Color.FromArgb(red1, green1, blue1));
                red1 = this.clamp(red1 + this.r.Next(-10, 10), 0, (int) byte.MaxValue);
                green1 = this.clamp(green1 + this.r.Next(-10, 10), 0, (int) byte.MaxValue);
                blue1 = this.clamp(blue1 + this.r.Next(-10, 10), 0, (int) byte.MaxValue);
            }
        }

        public void AddColor(int r, int g, int b)
        {
            this.ColorsGradient.Add(Color.FromArgb(r, g, b));
        }

        public void ResetColors()
        {
            this.ColorsGradient = new List<Color>();
        }

        public void AddColorLat(int r, int g, int b)
        {
            this.ColorsGradientLat.Add(Color.FromArgb(r, g, b));
        }

        public void ResetColorsLat()
        {
            this.ColorsGradientLat = new List<Color>();
        }

        private Color getGreyScale(int i, int j, float lat)
        {
            float num = this.getV(i, j);
            if ((double) num < 0.0)
                num = 0.0f;
            if ((double) num > (double) byte.MaxValue)
                num = (float) byte.MaxValue;
            return Color.FromArgb((int) num, (int) num, (int) num);
        }

        private Color getGreyScale(int i, int j, int k, float lat)
        {
            float num = this.getV(i, j, k);
            if ((double) num < 0.0)
                num = 0.0f;
            if ((double) num > (double) byte.MaxValue)
                num = (float) byte.MaxValue;
            return Color.FromArgb((int) num, (int) num, (int) num);
        }

        private Color getAlphaScale(int i, int j, float lat)
        {
            float num = this.getV(i, j);
            if ((double) num < 0.0)
                num = 0.0f;
            if ((double) num > (double) byte.MaxValue)
                num = (float) byte.MaxValue;
            return Color.FromArgb((int) num, (int) byte.MaxValue, (int) byte.MaxValue, (int) byte.MaxValue);
        }

        private Color getAlphaScale(int i, int j, int k, float lat)
        {
            float num = this.getV(i, j, k);
            if ((double) num < 0.0)
                num = 0.0f;
            if ((double) num > (double) byte.MaxValue)
                num = (float) byte.MaxValue;
            return Color.FromArgb((int) num, (int) byte.MaxValue, (int) byte.MaxValue, (int) byte.MaxValue);
        }

        private Color getColorScale(int i, int j, float lat)
        {
            float num1 = this.getV(i, j);
            if ((double) num1 < 0.0)
                num1 = 0.0f;
            if ((double) num1 > (double) byte.MaxValue)
                num1 = (float) byte.MaxValue;
            float num2 = num1 / (float) byte.MaxValue;
            float num3 = (float) (this.ColorsGradient.Count - 1) * num2;
            int index1 = (int) num3;
            int index2 = index1 + 1;
            if (index2 == this.ColorsGradient.Count)
                index2 = this.ColorsGradient.Count - 1;
            float num4 = (double) index1 < (double) num2 ? num2 - (float) (int) num2 : num3 - (float) index1;
            return
                Color.FromArgb(
                    (int)
                    ((double) this.ColorsGradient[index1].R * (1.0 - (double) num4) +
                     (double) this.ColorsGradient[index2].R * (double) num4),
                    (int)
                    ((double) this.ColorsGradient[index1].G * (1.0 - (double) num4) +
                     (double) this.ColorsGradient[index2].G * (double) num4),
                    (int)
                    ((double) this.ColorsGradient[index1].B * (1.0 - (double) num4) +
                     (double) this.ColorsGradient[index2].B * (double) num4));
        }

        private Color getColorScale(int i, int j, int k, float lat)
        {
            float num1 = this.getV(i, j, k);
            if ((double) num1 < 0.0)
                num1 = 0.0f;
            if ((double) num1 > (double) byte.MaxValue)
                num1 = (float) byte.MaxValue;
            float num2 = num1 / (float) byte.MaxValue;
            float num3 = (float) (this.ColorsGradient.Count - 1) * num2;
            int index1 = (int) num3;
            int index2 = index1 + 1;
            if (index2 == this.ColorsGradient.Count)
                index2 = this.ColorsGradient.Count - 1;
            float num4 = (double) index1 < (double) num2 ? num2 - (float) (int) num2 : num3 - (float) index1;
            return
                Color.FromArgb(
                    (int)
                    ((double) this.ColorsGradient[index1].R * (1.0 - (double) num4) +
                     (double) this.ColorsGradient[index2].R * (double) num4),
                    (int)
                    ((double) this.ColorsGradient[index1].G * (1.0 - (double) num4) +
                     (double) this.ColorsGradient[index2].G * (double) num4),
                    (int)
                    ((double) this.ColorsGradient[index1].B * (1.0 - (double) num4) +
                     (double) this.ColorsGradient[index2].B * (double) num4));
        }

        private Color getColorScaleLat(int i, int j, float lat)
        {
            float num1 = this.getV(i, j);
            if ((double) num1 < 0.0)
                num1 = 0.0f;
            if ((double) num1 > (double) byte.MaxValue)
                num1 = (float) byte.MaxValue;
            float num2 = num1 / (float) byte.MaxValue;
            float num3 = (float) (this.ColorsGradient.Count - 1) * num2;
            int index1 = (int) num3;
            int index2 = index1 + 1;
            if (index2 == this.ColorsGradient.Count)
                index2 = this.ColorsGradient.Count - 1;
            float num4 = (double) index1 < (double) num2 ? num2 - (float) (int) num2 : num3 - (float) index1;
            int num5 =
                (int)
                ((double) this.ColorsGradient[index1].R * (1.0 - (double) num4) +
                 (double) this.ColorsGradient[index2].R * (double) num4);
            int num6 =
                (int)
                ((double) this.ColorsGradient[index1].G * (1.0 - (double) num4) +
                 (double) this.ColorsGradient[index2].G * (double) num4);
            int num7 =
                (int)
                ((double) this.ColorsGradient[index1].B * (1.0 - (double) num4) +
                 (double) this.ColorsGradient[index2].B * (double) num4);
            int num8 =
                (int)
                ((double) this.ColorsGradientLat[index1 % this.ColorsGradientLat.Count].R * (1.0 - (double) num4) +
                 (double) this.ColorsGradientLat[index2 % this.ColorsGradientLat.Count].R * (double) num4);
            int num9 =
                (int)
                ((double) this.ColorsGradientLat[index1 % this.ColorsGradientLat.Count].G * (1.0 - (double) num4) +
                 (double) this.ColorsGradientLat[index2 % this.ColorsGradientLat.Count].G * (double) num4);
            int num10 =
                (int)
                ((double) this.ColorsGradientLat[index1 % this.ColorsGradientLat.Count].B * (1.0 - (double) num4) +
                 (double) this.ColorsGradientLat[index2 % this.ColorsGradientLat.Count].B * (double) num4);
            return
                Color.FromArgb(
                    this.clamp((int) ((double) num5 * (1.0 - (double) lat) + (double) num8 * (double) lat), 0,
                        (int) byte.MaxValue),
                    this.clamp((int) ((double) num6 * (1.0 - (double) lat) + (double) num9 * (double) lat), 0,
                        (int) byte.MaxValue),
                    this.clamp((int) ((double) num7 * (1.0 - (double) lat) + (double) num10 * (double) lat), 0,
                        (int) byte.MaxValue));
        }

        private Color getColorScaleLatCB(int i, int j, int k, float lat)
        {
            float num1 = this.getV(i, j, k);
            if ((double) num1 < 0.0)
                num1 = 0.0f;
            if ((double) num1 > (double) byte.MaxValue)
                num1 = (float) byte.MaxValue;
            float num2 = num1 / (float) byte.MaxValue;
            float num3 = (float) (this.ColorsGradient.Count - 1) * num2;
            int index1 = (int) num3;
            int index2 = index1 + 1;
            if (index2 == this.ColorsGradient.Count)
                index2 = this.ColorsGradient.Count - 1;
            float num4 = (double) index1 < (double) num2 ? num2 - (float) (int) num2 : num3 - (float) index1;
            int num5 =
                (int)
                ((double) this.ColorsGradient[index1].R * (1.0 - (double) num4) +
                 (double) this.ColorsGradient[index2].R * (double) num4);
            int num6 =
                (int)
                ((double) this.ColorsGradient[index1].G * (1.0 - (double) num4) +
                 (double) this.ColorsGradient[index2].G * (double) num4);
            int num7 =
                (int)
                ((double) this.ColorsGradient[index1].B * (1.0 - (double) num4) +
                 (double) this.ColorsGradient[index2].B * (double) num4);
            int num8 =
                (int)
                ((double) this.ColorsGradientLat[index1 % this.ColorsGradientLat.Count].R * (1.0 - (double) num4) +
                 (double) this.ColorsGradientLat[index2 % this.ColorsGradientLat.Count].R * (double) num4);
            int num9 =
                (int)
                ((double) this.ColorsGradientLat[index1 % this.ColorsGradientLat.Count].G * (1.0 - (double) num4) +
                 (double) this.ColorsGradientLat[index2 % this.ColorsGradientLat.Count].G * (double) num4);
            int num10 =
                (int)
                ((double) this.ColorsGradientLat[index1 % this.ColorsGradientLat.Count].B * (1.0 - (double) num4) +
                 (double) this.ColorsGradientLat[index2 % this.ColorsGradientLat.Count].B * (double) num4);
            return
                Color.FromArgb(
                    this.clamp((int) ((double) num5 * (1.0 - (double) lat) + (double) num8 * (double) lat), 0,
                        (int) byte.MaxValue),
                    this.clamp((int) ((double) num6 * (1.0 - (double) lat) + (double) num9 * (double) lat), 0,
                        (int) byte.MaxValue),
                    this.clamp((int) ((double) num7 * (1.0 - (double) lat) + (double) num10 * (double) lat), 0,
                        (int) byte.MaxValue));
        }

        public Bitmap getBitmap(Gen.getPixelDelegate f)
        {
            return this.getBitmap(0, this.wid - 1, 0, this.hei - 1, f);
        }

        public Bitmap getBitmap(int startX, int endX, int startY, int endY, Gen.getPixelDelegate f)
        {
            startX %= this.wid;
            startX = Math.Abs(startX);
            endX %= this.wid;
            endX = Math.Abs(endX);
            startY %= this.hei;
            startY = Math.Abs(startY);
            endY %= this.hei;
            endY = Math.Abs(endY);
            if (startX > endX)
            {
                int num = startX;
                startX = endX;
                endX = num;
            }
            if (startY > endY)
            {
                int num = startY;
                startY = endY;
                endY = num;
            }
            int w = endX - startX + 1;
            int h = endY - startY + 1;
            Bitmap bitmap = this.createBitmap(w, h);
            if (bitmap == null)
                return (Bitmap) null;
            for (int x = 0; x < w; ++x)
            {
                for (int y = 0; y < h; ++y)
                {
                    float lat;
                    if (this.isCB)
                    {
                        lat = 0.0f;
                        float num = (float) this.hei / 3f;
                        if (startX >= this.wid / 3 && startX <= this.wid / 3 * 2)
                        {
                            if (startY >= 0 && startY <= this.hei / 3)
                                lat = 1f;
                            if (startY >= this.hei / 3 * 2 && startY <= this.hei)
                                lat = 1f;
                        }
                        else
                            lat = Math.Abs((float) ((double) y / ((double) num / 2.0) - 1.0)) * this.Lat01Factor;
                    }
                    else
                        lat = Math.Abs((float) ((double) y / ((double) this.hei / 2.0) - 1.0)) * this.Lat01Factor;
                    bitmap.SetPixel(x, y, f(startX + x, startY + y, lat));
                }
            }
            return bitmap;
        }

        private float calcLatZ0_Z1_X0_X1(int y, int h)
        {
            return Math.Abs((float) ((double) y / ((double) h / 2.0) - 1.0)) / 2f;
        }

        private float calcLatY0_Y1(int y, int h)
        {
            return 1f - Math.Abs((float) y / (float) h - 0.5f);
        }

        public Bitmap getBitmapCB(int face, Gen.getPixelDelegateCB f)
        {
            Bitmap bitmap = this.createBitmap(this.wid, this.hei);
            if (bitmap == null)
                return (Bitmap) null;
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < this.hei; ++index2)
                {
                    float lat1 = this.calcLatZ0_Z1_X0_X1(index2, this.hei);
                    if (face == 0)
                        bitmap.SetPixel(index1, index2, f(index1, index2, 0, lat1));
                    else if (face == 1)
                        bitmap.SetPixel(index1, index2, f(this.wid - 1 - index1, index2, this.wid - 1, lat1));
                    else if (face == 2)
                        bitmap.SetPixel(index1, index2, f(this.wid - 1, index2, index1, lat1));
                    else if (face == 3)
                        bitmap.SetPixel(index1, index2, f(0, index2, this.wid - 1 - index1, lat1));
                    else if (face == 4)
                    {
                        float val2 = this.calcLatY0_Y1(index2, this.hei);
                        float lat2 = Math.Min(this.calcLatY0_Y1(index1, this.wid), val2);
                        bitmap.SetPixel(index1, index2, f(index1, 0, this.wid - 1 - index2, lat2));
                    }
                    else if (face == 5)
                    {
                        float val2 = this.calcLatY0_Y1(index2, this.hei);
                        float lat2 = Math.Min(this.calcLatY0_Y1(index1, this.wid), val2);
                        bitmap.SetPixel(index1, index2, f(index1, this.wid - 1, index2, lat2));
                    }
                }
            }
            return bitmap;
        }

        public Bitmap getBitmapCB(Gen.getPixelDelegateCB f)
        {
            Bitmap bitmap = this.createBitmap(this.wid * 3, this.hei * 3);
            if (bitmap == null)
                return (Bitmap) null;
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < this.hei; ++index2)
                {
                    float lat1 = 0.0f;
                    float lat2 = 0.0f;
                    if (this.ColorsGradientLat.Count > 0)
                    {
                        lat1 = this.calcLatZ0_Z1_X0_X1(index2, this.hei);
                        float val2 = this.calcLatY0_Y1(index2, this.hei);
                        lat2 = Math.Min(this.calcLatY0_Y1(index1, this.wid), val2);
                    }
                    bitmap.SetPixel(this.wid + index1, this.wid + index2, f(index1, index2, 0, lat1));
                    bitmap.SetPixel(this.wid * 2 + index1, this.wid * 2 + index2,
                        f(this.wid - 1 - index1, index2, this.wid - 1, lat1));
                    bitmap.SetPixel(this.wid * 2 + index1, this.wid + index2, f(this.wid - 1, index2, index1, lat1));
                    bitmap.SetPixel(index1, this.wid + index2, f(0, index2, this.wid - 1 - index1, lat1));
                    bitmap.SetPixel(this.wid + index1, index2, f(index1, 0, this.wid - 1 - index2, lat2));
                    bitmap.SetPixel(this.wid + index1, this.wid * 2 + index2, f(index1, this.wid - 1, index2, lat2));
                }
            }
            return bitmap;
        }

        public Bitmap getBitmap(bool greyscale)
        {
            if (greyscale)
                return this.getBitmap(new Gen.getPixelDelegate(this.getGreyScale));
            return this.getBitmap(new Gen.getPixelDelegate(this.getAlphaScale));
        }

        private Bitmap OLDgetBitmap(Color fromC, Color toC)
        {
            Bitmap bitmap = this.createBitmap(this.wid, this.hei);
            if (bitmap == null)
                return (Bitmap) null;
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < this.hei; ++index2)
                {
                    float num1 = this.getV(index1, index2);
                    if ((double) num1 < 0.0)
                        num1 = 0.0f;
                    if ((double) num1 > (double) byte.MaxValue)
                        num1 = (float) byte.MaxValue;
                    float num2 = num1 / (float) byte.MaxValue;
                    Color color =
                        Color.FromArgb(
                            (int) ((double) fromC.R * (1.0 - (double) num2) + (double) toC.R * (double) num2),
                            (int) ((double) fromC.G * (1.0 - (double) num2) + (double) toC.G * (double) num2),
                            (int) ((double) fromC.B * (1.0 - (double) num2) + (double) toC.B * (double) num2));
                    bitmap.SetPixel(index1, index2, color);
                }
            }
            return bitmap;
        }

        public Bitmap getBitmap()
        {
            if (this.ColorsGradient.Count <= 1)
                return this.getBitmap(true);
            if (this.ColorsGradientLat.Count > 1)
                return this.getBitmap(new Gen.getPixelDelegate(this.getColorScaleLat));
            return this.getBitmap(new Gen.getPixelDelegate(this.getColorScale));
        }

        public Bitmap getBitmap(List<Color> c)
        {
            Bitmap bitmap = this.createBitmap(this.wid, this.hei);
            if (bitmap == null)
                return (Bitmap) null;
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < this.hei; ++index2)
                {
                    float num1 = this.getV(index1, index2);
                    if ((double) num1 < 0.0)
                        num1 = 0.0f;
                    if ((double) num1 > (double) byte.MaxValue)
                        num1 = (float) byte.MaxValue;
                    float num2 = num1 / (float) byte.MaxValue;
                    int index3 = (int) (((double) c.Count - 1.0) * (double) num2);
                    int index4 = index3 + 1;
                    if (index3 < 0)
                        index3 = 0;
                    if (index4 == c.Count)
                        index4 = c.Count - 1;
                    float num3 = (float) index3 / ((float) c.Count - 1f);
                    float num4 = (float) index4 / ((float) c.Count - 1f);
                    int red;
                    int green;
                    int blue;
                    if ((double) num4 - (double) num3 == 0.0)
                    {
                        red = (int) c[index3].R;
                        green = (int) c[index3].G;
                        blue = (int) c[index3].B;
                    }
                    else
                    {
                        float num5 = (float) (((double) num2 - (double) num3) / ((double) num4 - (double) num3));
                        red =
                            (int) ((double) c[index3].R * (1.0 - (double) num5) + (double) c[index4].R * (double) num5);
                        green =
                            (int) ((double) c[index3].G * (1.0 - (double) num5) + (double) c[index4].G * (double) num5);
                        blue =
                            (int) ((double) c[index3].B * (1.0 - (double) num5) + (double) c[index4].B * (double) num5);
                    }
                    Color color = Color.FromArgb(red, green, blue);
                    bitmap.SetPixel(index1, index2, color);
                }
            }
            return bitmap;
        }

        public Bitmap getBitmap(List<Color> c, List<Color> cLat, float lat01factor)
        {
            Bitmap bitmap = this.createBitmap(this.wid, this.hei);
            if (bitmap == null)
                return (Bitmap) null;
            lat01factor = this.clamp(lat01factor, 0.0f, 1f);
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < this.hei; ++index2)
                {
                    float num1 = this.getV(index1, index2);
                    if ((double) num1 < 0.0)
                        num1 = 0.0f;
                    if ((double) num1 > (double) byte.MaxValue)
                        num1 = (float) byte.MaxValue;
                    float num2 = num1 / (float) byte.MaxValue;
                    int index3 = (int) (((double) c.Count - 1.0) * (double) num2);
                    int index4 = index3 + 1;
                    if (index3 < 0)
                        index3 = 0;
                    if (index4 == c.Count)
                        index4 = c.Count - 1;
                    float num3 = (float) index3 / ((float) c.Count - 1f);
                    float num4 = (float) index4 / ((float) c.Count - 1f);
                    int num5;
                    int num6;
                    int num7;
                    if ((double) num4 - (double) num3 == 0.0)
                    {
                        num5 = (int) c[index3].R;
                        num6 = (int) c[index3].G;
                        num7 = (int) c[index3].B;
                    }
                    else
                    {
                        num2 = (float) (((double) num2 - (double) num3) / ((double) num4 - (double) num3));
                        num5 =
                            (int) ((double) c[index3].R * (1.0 - (double) num2) + (double) c[index4].R * (double) num2);
                        num6 =
                            (int) ((double) c[index3].G * (1.0 - (double) num2) + (double) c[index4].G * (double) num2);
                        num7 =
                            (int) ((double) c[index3].B * (1.0 - (double) num2) + (double) c[index4].B * (double) num2);
                    }
                    int num8;
                    int num9;
                    int num10;
                    if ((double) num4 - (double) num3 == 0.0)
                    {
                        num8 = (int) cLat[index3 % cLat.Count].R;
                        num9 = (int) cLat[index3 % cLat.Count].G;
                        num10 = (int) cLat[index3 % cLat.Count].B;
                    }
                    else
                    {
                        float num11 = (float) (((double) num2 - (double) num3) / ((double) num4 - (double) num3));
                        num8 =
                            (int)
                            ((double) cLat[index3 % cLat.Count].R * (1.0 - (double) num11) +
                             (double) cLat[index4 % cLat.Count].R * (double) num11);
                        num9 =
                            (int)
                            ((double) cLat[index3 % cLat.Count].G * (1.0 - (double) num11) +
                             (double) cLat[index4 % cLat.Count].G * (double) num11);
                        num10 =
                            (int)
                            ((double) cLat[index3 % cLat.Count].B * (1.0 - (double) num11) +
                             (double) cLat[index4 % cLat.Count].B * (double) num11);
                    }
                    float num12 = Math.Abs((float) ((double) index2 / ((double) this.hei / 2.0) - 1.0)) * lat01factor;
                    Color color =
                        Color.FromArgb(
                            this.clamp((int) ((double) num5 * (1.0 - (double) num12) + (double) num8 * (double) num12),
                                0, (int) byte.MaxValue),
                            this.clamp((int) ((double) num6 * (1.0 - (double) num12) + (double) num9 * (double) num12),
                                0, (int) byte.MaxValue),
                            this.clamp(
                                (int) ((double) num7 * (1.0 - (double) num12) + (double) num10 * (double) num12), 0,
                                (int) byte.MaxValue));
                    bitmap.SetPixel(index1, index2, color);
                }
            }
            return bitmap;
        }

        public Bitmap getBitmap3d(int k)
        {
            if (this.ColorsGradient.Count > 1)
                return this.getBitmap3d(this.ColorsGradient, k);
            return this.getBitmap3d(true, k);
        }

        public Bitmap getBitmap3d(bool greyscale, int k)
        {
            Bitmap bitmap = this.createBitmap(this.wid, this.hei);
            if (bitmap == null)
                return (Bitmap) null;
            if (this.is3d() && k < this.zlvl)
            {
                for (int index1 = 0; index1 < this.wid; ++index1)
                {
                    for (int index2 = 0; index2 < this.hei; ++index2)
                    {
                        float num = this.getV(index1, index2, k);
                        if ((double) num < 0.0)
                            num = 0.0f;
                        if ((double) num > (double) byte.MaxValue)
                            num = (float) byte.MaxValue;
                        Color color = !greyscale
                            ? Color.FromArgb((int) num, (int) byte.MaxValue, (int) byte.MaxValue, (int) byte.MaxValue)
                            : Color.FromArgb((int) num, (int) num, (int) num);
                        bitmap.SetPixel(index1, index2, color);
                    }
                }
            }
            return bitmap;
        }

        private Color calcNormalCB(int x, int y, int face, float bump)
        {
            float num1 = 0.0f;
            float num2 = 0.0f;
            float num3 = 0.0f;
            float num4 = 0.0f;
            if (face == 0)
            {
                num1 = this.getPix01NoSeam(x - 1, y, 0);
                num2 = this.getPix01NoSeam(x + 1, y, 0);
                num3 = this.getPix01NoSeam(x, y - 1, 0);
                num4 = this.getPix01NoSeam(x, y + 1, 0);
            }
            else if (face == 1)
            {
                num1 = this.getPix01NoSeam(x - 1, y, this.wid - 1);
                num2 = this.getPix01NoSeam(x + 1, y, this.wid - 1);
                num3 = this.getPix01NoSeam(x, y - 1, this.wid - 1);
                num4 = this.getPix01NoSeam(x, y + 1, this.wid - 1);
            }
            else if (face == 2)
            {
                num1 = this.getPix01NoSeam(this.wid - 1, y, x - 1);
                num2 = this.getPix01NoSeam(this.wid - 1, y, x + 1);
                num3 = this.getPix01NoSeam(this.wid - 1, y - 1, x);
                num4 = this.getPix01NoSeam(this.wid - 1, y + 1, x);
            }
            else if (face == 3)
            {
                num1 = this.getPix01NoSeam(0, y, x + 1);
                num2 = this.getPix01NoSeam(0, y, x - 1);
                num3 = this.getPix01NoSeam(0, y - 1, x);
                num4 = this.getPix01NoSeam(0, y + 1, x);
            }
            else if (face == 4)
            {
                num1 = this.getPix01NoSeam(x - 1, 0, y);
                num2 = this.getPix01NoSeam(x + 1, 0, y);
                num3 = this.getPix01NoSeam(x, 0, y + 1);
                num4 = this.getPix01NoSeam(x, 0, y - 1);
            }
            else if (face == 5)
            {
                num1 = this.getPix01NoSeam(x - 1, this.wid - 1, y);
                num2 = this.getPix01NoSeam(x + 1, this.wid - 1, y);
                num3 = this.getPix01NoSeam(x, this.wid - 1, y - 1);
                num4 = this.getPix01NoSeam(x, this.wid - 1, y + 1);
            }
            float num5 = (float) (0.5 + ((double) num1 - (double) num2) * (double) bump);
            float num6 = (float) (0.5 + ((double) num3 - (double) num4) * (double) bump);
            if ((double) num5 < 0.0)
                num5 = 0.0f;
            if ((double) num5 > 1.0)
                num5 = 1f;
            if ((double) num6 < 0.0)
                num6 = 0.0f;
            if ((double) num6 > 1.0)
                num6 = 1f;
            return Color.FromArgb((int) ((double) num5 * (double) byte.MaxValue),
                (int) ((double) num6 * (double) byte.MaxValue), (int) byte.MaxValue);
        }

        public Bitmap getNormal(float bumpness)
        {
            Bitmap bitmap = this.createBitmap(this.wid, this.hei);
            if (bitmap == null)
                return (Bitmap) null;
            for (int index1 = 0; index1 < this.hei; ++index1)
            {
                for (int index2 = 0; index2 < this.wid; ++index2)
                {
                    float pix01_1 = this.getPix01(index2 - 1, index1);
                    float pix01_2 = this.getPix01(index2 + 1, index1);
                    float pix01_3 = this.getPix01(index2, index1 - 1);
                    float pix01_4 = this.getPix01(index2, index1 + 1);
                    float num1 = (float) (0.5 + ((double) pix01_1 - (double) pix01_2) * (double) bumpness);
                    float num2 = (float) (0.5 + ((double) pix01_3 - (double) pix01_4) * (double) bumpness);
                    if ((double) num1 < 0.0)
                        num1 = 0.0f;
                    if ((double) num1 > 1.0)
                        num1 = 1f;
                    if ((double) num2 < 0.0)
                        num2 = 0.0f;
                    if ((double) num2 > 1.0)
                        num2 = 1f;
                    bitmap.SetPixel(index2, index1,
                        Color.FromArgb((int) ((double) num1 * (double) byte.MaxValue),
                            (int) ((double) num2 * (double) byte.MaxValue), (int) byte.MaxValue));
                }
            }
            return bitmap;
        }

        public Bitmap getNormal(int face, float bumpness)
        {
            Bitmap bitmap = this.createBitmap(this.wid, this.hei);
            if (bitmap == null)
                return (Bitmap) null;
            if (this.isCB)
            {
                for (int x = 0; x < this.wid; ++x)
                {
                    for (int y = 0; y < this.hei; ++y)
                    {
                        if (face == 0)
                            bitmap.SetPixel(x, y, this.calcNormalCB(x, y, face, bumpness));
                        else if (face == 1)
                            bitmap.SetPixel(x, y, this.calcNormalCB(this.wid - x, y, face, bumpness));
                        else if (face == 2)
                            bitmap.SetPixel(x, y, this.calcNormalCB(x, y, face, bumpness));
                        else if (face == 3)
                            bitmap.SetPixel(x, y, this.calcNormalCB(this.wid - x, y, face, bumpness));
                        else if (face == 4)
                            bitmap.SetPixel(x, y, this.calcNormalCB(x, this.wid - y, face, bumpness));
                        else if (face == 5)
                            bitmap.SetPixel(x, y, this.calcNormalCB(x, y, face, bumpness));
                    }
                }
            }
            return bitmap;
        }

        public Bitmap getNormalCBFull(float bumpness)
        {
            Bitmap bitmap = this.createBitmap(this.wid * 3, this.hei * 3);
            if (bitmap == null)
                return (Bitmap) null;
            if (this.isCB)
            {
                for (int x = 0; x < this.wid; ++x)
                {
                    for (int y = 0; y < this.hei; ++y)
                    {
                        bitmap.SetPixel(this.wid + x, this.wid + y, this.calcNormalCB(x, y, 0, bumpness));
                        bitmap.SetPixel(this.wid * 2 + x, this.wid * 2 + y,
                            this.calcNormalCB(this.wid - x, y, 1, bumpness));
                        bitmap.SetPixel(this.wid * 2 + x, this.wid + y, this.calcNormalCB(x, y, 2, bumpness));
                        bitmap.SetPixel(x, this.wid + y, this.calcNormalCB(this.wid - x, y, 3, bumpness));
                        bitmap.SetPixel(this.wid + x, y, this.calcNormalCB(x, this.wid - y, 4, bumpness));
                        bitmap.SetPixel(this.wid + x, this.wid * 2 + y, this.calcNormalCB(x, y, 5, bumpness));
                    }
                }
            }
            return bitmap;
        }

        public Bitmap getBitmap3d(List<Color> c, int k)
        {
            Bitmap bitmap = this.createBitmap(this.wid, this.hei);
            if (bitmap == null)
                return (Bitmap) null;
            if (this.is3d() && k < this.zlvl)
            {
                for (int index1 = 0; index1 < this.wid; ++index1)
                {
                    for (int index2 = 0; index2 < this.hei; ++index2)
                    {
                        float num1 = this.getV(index1, index2, k);
                        if ((double) num1 < 0.0)
                            num1 = 0.0f;
                        if ((double) num1 > (double) byte.MaxValue)
                            num1 = (float) byte.MaxValue;
                        float num2 = num1 / (float) byte.MaxValue;
                        int index3 = (int) (((double) c.Count - 1.0) * (double) num2);
                        int index4 = index3 + 1;
                        if (index3 < 0)
                            index3 = 0;
                        if (index4 == c.Count)
                            index4 = c.Count - 1;
                        float num3 = (float) index3 / ((float) c.Count - 1f);
                        float num4 = (float) index4 / ((float) c.Count - 1f);
                        int red;
                        int green;
                        int blue;
                        if ((double) num4 - (double) num3 == 0.0)
                        {
                            red = (int) c[index3].R;
                            green = (int) c[index3].G;
                            blue = (int) c[index3].B;
                        }
                        else
                        {
                            float num5 = (float) (((double) num2 - (double) num3) / ((double) num4 - (double) num3));
                            red =
                                (int)
                                ((double) c[index3].R * (1.0 - (double) num5) + (double) c[index4].R * (double) num5);
                            green =
                                (int)
                                ((double) c[index3].G * (1.0 - (double) num5) + (double) c[index4].G * (double) num5);
                            blue =
                                (int)
                                ((double) c[index3].B * (1.0 - (double) num5) + (double) c[index4].B * (double) num5);
                        }
                        Color color = Color.FromArgb(red, green, blue);
                        bitmap.SetPixel(index1, index2, color);
                    }
                }
            }
            return bitmap;
        }

        private void storeBitmap(string fname, Bitmap B)
        {
            if (this.Bitmaps == null || !this.storeBitmaps)
                return;
            if (this.Bitmaps.ContainsKey(fname))
                this.Bitmaps[fname] = B;
            else
                this.Bitmaps.Add(fname, B);
        }

        private Bitmap createBitmap(int w, int h)
        {
            Bitmap bitmap = (Bitmap) null;
            try
            {
                bitmap = new Bitmap(w, h);
            }
            catch
            {
                this.LogMsg(ref this.log, "Error in creating Bitmap! Problably bitmap too large!");
            }
            return bitmap;
        }

        private int clamp(int v, int min, int max)
        {
            if (v > max)
                return max;
            if (v < min)
                return min;
            return v;
        }

        private float clamp(float v, float min, float max)
        {
            if ((double) v > (double) max)
                return max;
            if ((double) v < (double) min)
                return min;
            return v;
        }

        private double clamp(double v, double min, double max)
        {
            if (v > max)
                return max;
            if (v < min)
                return min;
            return v;
        }

        public float[,] getValues()
        {
            float[,] numArray = new float[this.wid, this.hei];
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                    numArray[i, j] = this.getV(i, j);
            }
            return numArray;
        }

        public float[,,] getValues3D()
        {
            float[,,] numArray = new float[this.wid, this.hei, this.zlvl];
            for (int i = 0; i < this.wid; ++i)
            {
                for (int j = 0; j < this.hei; ++j)
                {
                    for (int k = 0; k < this.zlvl; ++k)
                        numArray[i, j, k] = this.getV(i, j, k);
                }
            }
            return numArray;
        }

        public float[] getValues1D()
        {
            return this._Values1D;
        }

        public bool is3d()
        {
            return this.n_dimensions == 3;
        }

        public int getDimension()
        {
            return this.n_dimensions;
        }

        public int nextRandom()
        {
            return this.r.Next();
        }

        public void setSeed(int seed)
        {
            this.Seed = seed;
            this.r = new Random(seed);
        }

        public void seamlesON()
        {
            this.seamlessX = true;
            this.seamlessY = true;
            this.seamlessZ = true;
        }

        public void seamlesOFF()
        {
            this.seamlessX = false;
            this.seamlessY = false;
            this.seamlessZ = false;
        }

        public void seamlesXON()
        {
            this.seamlessX = true;
        }

        public void seamlesXOFF()
        {
            this.seamlessX = false;
        }

        public void seamlesYON()
        {
            this.seamlessY = true;
        }

        public void seamlesYOFF()
        {
            this.seamlessY = false;
        }

        public void seamlesZON()
        {
            this.seamlessZ = true;
        }

        public void seamlesZOFF()
        {
            this.seamlessZ = false;
        }

        public void setMargin2d(int startMarX, int endMarX, int startMarY, int endMarY)
        {
            if (startMarX < this.wid - endMarX && startMarX >= 0 && endMarX >= 0)
            {
                this.startMarginX = startMarX;
                this.endMarginX = endMarX;
            }
            if (startMarY >= this.hei - endMarY || startMarY < 0 || endMarY < 0)
                return;
            this.startMarginY = startMarY;
            this.endMarginY = endMarY;
        }

        public void setMargin3d(int startMarX, int endMarX, int startMarY, int endMarY, int startMarZ, int endMarZ)
        {
            this.setMargin2d(startMarX, endMarX, startMarY, endMarY);
            if (startMarZ >= this.zlvl - endMarZ || startMarZ < 0 || endMarZ < 0)
                return;
            this.startMarginZ = startMarZ;
            this.endMarginZ = endMarZ;
        }

        private void LogMsg(ref string a, string msg)
        {
            a = a + msg + Environment.NewLine;
        }

        public string exe()
        {
            return this.exeScript(this.Script);
        }

        public string getLog()
        {
            return this.log;
        }

        public string exeScript(string script)
        {
            this.Bitmaps = new Dictionary<string, Bitmap>();
            this.Script = script;
            this.log = "";
            this.error = false;
            Dictionary<string, Gen> dictionary = new Dictionary<string, Gen>();
            string[] strArray1 = script.Split(new string[3]
            {
                "\r\n",
                ";",
                "\n"
            }, StringSplitOptions.None);
            Type type = typeof(Gen);
            int num = 0;
            foreach (string str1 in strArray1)
            {
                ++num;
                string str2 = str1.Trim();
                if (str2.Trim() != "" && !str2.Trim().StartsWith("//"))
                {
                    string[] strArray2 =
                        Regex.Replace(str2.Split(new string[1] {"//"}, StringSplitOptions.None)[0].Trim(), "\\s+", " ")
                            .Split((char[]) null);
                    string name = strArray2[0];
                    object[] parameters1 = new object[strArray2.GetLength(0) - 1];
                    MethodInfo method = type.GetMethod(name);
                    Gen cntNoise1 = this;
                    if ((object) method == null && name.Contains("->"))
                    {
                        string[] strArray3 = name.Split(new string[1] {"->"}, StringSplitOptions.None);
                        string key = strArray3[0];
                        name = strArray3[1];
                        method = type.GetMethod(name);
                        Gen cntNoise2 = (Gen) null;
                        if (dictionary.ContainsKey(key))
                        {
                            cntNoise2 = dictionary[key];
                        }
                        else
                        {
                            if (this.getDimension() == 1)
                                cntNoise2 = new Gen(this.wid, this.nextRandom());
                            else if (this.getDimension() == 2)
                                cntNoise2 = new Gen(this.wid, this.hei, this.nextRandom());
                            else if (this.getDimension() == 3)
                            {
                                cntNoise2 = new Gen(this.wid, this.hei, this.zlvl, this.nextRandom());
                                if (this.isCB)
                                    cntNoise2.setCB(this.wid);
                            }
                            dictionary.Add(key, cntNoise2);
                        }
                        cntNoise1 = cntNoise2;
                    }
                    if ((object) method == null)
                    {
                        this.LogMsg(ref this.log, name + " method not valid! in line " + num.ToString());
                        this.error = true;
                        return this.log;
                    }
                    ParameterInfo[] parameters2 = method.GetParameters();
                    if (parameters2.GetLength(0) != strArray2.GetLength(0) - 1)
                    {
                        this.LogMsg(ref this.log,
                            "Parameters needed: " + (object) parameters2.GetLength(0) + ", parameter used : " +
                            (strArray2.GetLength(0) - 1).ToString() + " in line " + num.ToString());
                        this.error = true;
                        return this.log;
                    }
                    for (int index = 1; index < strArray2.GetLength(0); ++index)
                    {
                        Type parameterType = parameters2[index - 1].ParameterType;
                        if ((object) parameterType == (object) typeof(int))
                        {
                            int result;
                            if (int.TryParse(strArray2[index], out result))
                            {
                                parameters1[index - 1] = (object) result;
                            }
                            else
                            {
                                this.LogMsg(ref this.log,
                                    " int Parameter n." + index.ToString() + " " + strArray2[index] +
                                    " not valid! in line " + num.ToString());
                                this.error = true;
                                return this.log;
                            }
                        }
                        if ((object) parameterType == (object) typeof(float))
                        {
                            char newChar = Convert.ToChar(CultureInfo.CurrentCulture.NumberFormat.NumberDecimalSeparator);
                            strArray2[index] = strArray2[index].Replace(',', '.');
                            strArray2[index] = strArray2[index].Replace('.', newChar);
                            float result;
                            if (float.TryParse(strArray2[index], out result))
                            {
                                parameters1[index - 1] = (object) result;
                            }
                            else
                            {
                                this.LogMsg(ref this.log,
                                    " float Parameter n." + index.ToString() + " " + strArray2[index] +
                                    " not valid! in line " + num.ToString());
                                this.error = true;
                                return this.log;
                            }
                        }
                        if ((object) parameterType == (object) typeof(double))
                        {
                            char newChar = Convert.ToChar(CultureInfo.CurrentCulture.NumberFormat.NumberDecimalSeparator);
                            strArray2[index] = strArray2[index].Replace(',', '.');
                            strArray2[index] = strArray2[index].Replace('.', newChar);
                            double result;
                            if (double.TryParse(strArray2[index], out result))
                            {
                                parameters1[index - 1] = (object) result;
                            }
                            else
                            {
                                this.LogMsg(ref this.log,
                                    " double Parameter n." + index.ToString() + " " + strArray2[index] +
                                    " not valid! in line " + num.ToString());
                                this.error = true;
                                return this.log;
                            }
                        }
                        if ((object) parameterType == (object) typeof(Gen))
                        {
                            if (dictionary.ContainsKey(strArray2[index]))
                            {
                                parameters1[index - 1] = (object) dictionary[strArray2[index]];
                            }
                            else
                            {
                                this.LogMsg(ref this.log,
                                    " NOISE Parameter n." + index.ToString() + " " + strArray2[index] +
                                    " not valid! in line " + num.ToString());
                                this.error = true;
                                return this.log;
                            }
                        }
                        if ((object) parameterType == (object) typeof(string))
                            parameters1[index - 1] = (object) strArray2[index];
                    }
                    this.timer = DateTime.Now;
                    method.Invoke((object) cntNoise1, parameters1);
                    if (this.error)
                        return this.log;
                    string str3 = Math.Round((DateTime.Now - this.timer).TotalSeconds, 3).ToString();
                    this.LogMsg(ref this.log,
                        "Op. " + method.Name + " " + str3 + " sec. B.W " + cntNoise1.getCSWrites() + " B.R " +
                        cntNoise1.getCSReads());
                }
            }
            return this.log;
        }

        public void Parse(string e)
        {
        }

        public void save(string fname)
        {
            fname = fname.Trim();
            if (!(fname != ""))
                return;
            if (!fname.Contains(".cnt"))
                fname += ".cnt";
            this.saveScript(fname + ".scri");
            Stream serializationStream = (Stream) File.Open(fname, FileMode.Create);
            new BinaryFormatter().Serialize(serializationStream, (object) this);
            serializationStream.Close();
        }

        public static Gen load(string fname)
        {
            Stream serializationStream = (Stream) File.Open(fname, FileMode.Open);
            Gen cntNoise = (Gen) new BinaryFormatter().Deserialize(serializationStream);
            serializationStream.Close();
            cntNoise.Script = cntNoise.Script.Replace("save ", "//save ");
            cntNoise.Script = cntNoise.Script.Replace("savePng ", "//savePng ");
            return cntNoise;
        }

        public void saveScript(string fname)
        {
            File.WriteAllText(fname, this.Script);
        }

        public void loadScript(string fname)
        {
            try
            {
                this.Script = File.ReadAllText(fname);
            }
            catch
            {
            }
        }

        public void saveNormal(string fname, float bump)
        {
            Bitmap normal = this.getNormal(bump);
            this.storeBitmap(fname, normal);
            if (fname.Contains(":\\"))
            {
                try
                {
                    normal.Save(fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " saveNormal : " + ex.Message);
                }
            }
            else
            {
                try
                {
                    normal.Save(Directory.GetCurrentDirectory() + "\\" + fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " saveNormal : " + ex.Message);
                }
            }
        }

        public void savePngCB_OLD(string fname, int grey)
        {
            int num = this.wid / 3;
            this.savePng(num, num * 2 - 1, 0, num - 1, fname + "_U", grey);
            this.savePng(0, num - 1, num, num * 2 - 1, fname + "_L", grey);
            this.savePng(num, num * 2 - 1, num, num * 2 - 1, fname + "_F", grey);
            this.savePng(num * 2, num * 3 - 1, num, num * 2 - 1, fname + "_R", grey);
            this.savePng(num, num * 2 - 1, num * 2, num * 3 - 1, fname + "_D", grey);
            this.savePng(num * 2, num * 3 - 1, num * 2, num * 3 - 1, fname + "_B", grey);
        }

        private void savePng(int startX, int endX, int startY, int endY, string fname, int grey)
        {
            Bitmap B = grey != 2
                ? (grey != 1
                    ? this.getBitmap(startX, endX, startY, endY, new Gen.getPixelDelegate(this.getAlphaScale))
                    : this.getBitmap(startX, endX, startY, endY, new Gen.getPixelDelegate(this.getGreyScale)))
                : this.getBitmap(startX, endX, startY, endY, new Gen.getPixelDelegate(this.getColorScale));
            this.storeBitmap(fname, B);
            if (fname.Contains(":\\"))
            {
                try
                {
                    B.Save(fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
            else
            {
                try
                {
                    B.Save(Directory.GetCurrentDirectory() + "\\" + fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
        }

        public void savePngCB(string fname, int grey)
        {
            this.savePngCB(0, fname + "_F", grey);
            this.savePngCB(1, fname + "_B", grey);
            this.savePngCB(2, fname + "_R", grey);
            this.savePngCB(3, fname + "_L", grey);
            this.savePngCB(4, fname + "_U", grey);
            this.savePngCB(5, fname + "_D", grey);
        }

        public void saveNormalCB(string fname, float bump)
        {
            this.saveNormalCB(0, fname + "_F_N", bump);
            this.saveNormalCB(1, fname + "_B_N", bump);
            this.saveNormalCB(2, fname + "_R_N", bump);
            this.saveNormalCB(3, fname + "_L_N", bump);
            this.saveNormalCB(4, fname + "_U_N", bump);
            this.saveNormalCB(5, fname + "_D_N", bump);
        }

        public void savePngCBFull(string fname, int grey)
        {
            Bitmap B = grey != 2
                ? (grey != 1
                    ? this.getBitmapCB(new Gen.getPixelDelegateCB(this.getAlphaScale))
                    : this.getBitmapCB(new Gen.getPixelDelegateCB(this.getGreyScale)))
                : (this.ColorsGradientLat.Count <= 0
                    ? this.getBitmapCB(new Gen.getPixelDelegateCB(this.getColorScale))
                    : this.getBitmapCB(new Gen.getPixelDelegateCB(this.getColorScaleLatCB)));
            this.storeBitmap(fname, B);
            if (fname.Contains(":\\"))
            {
                try
                {
                    B.Save(fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
            else
            {
                try
                {
                    B.Save(Directory.GetCurrentDirectory() + "\\" + fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
        }

        public void savePng3d(string fnameOri, int grey)
        {
            for (int k = 0; k < this.zlvl; ++k)
            {
                string fname = fnameOri + "_" + k.ToString();
                Bitmap bitmap3d = this.getBitmap3d(k);
                this.storeBitmap(fname, bitmap3d);
                if (fname.Contains(":\\"))
                {
                    try
                    {
                        bitmap3d.Save(fname + ".png", ImageFormat.Png);
                    }
                    catch (Exception ex)
                    {
                        this.LogMsg(ref this.log, " savePng : " + ex.Message);
                    }
                }
                else
                {
                    try
                    {
                        bitmap3d.Save(Directory.GetCurrentDirectory() + "\\" + fname + ".png", ImageFormat.Png);
                    }
                    catch (Exception ex)
                    {
                        this.LogMsg(ref this.log, " savePng : " + ex.Message);
                    }
                }
            }
        }

        public void saveNormalCBFull(string fname, float bump)
        {
            Bitmap normalCbFull = this.getNormalCBFull(bump);
            this.storeBitmap(fname, normalCbFull);
            if (fname.Contains(":\\"))
            {
                try
                {
                    normalCbFull.Save(fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
            else
            {
                try
                {
                    normalCbFull.Save(Directory.GetCurrentDirectory() + "\\" + fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
        }

        private void savePngCB(int face, string fname, int grey)
        {
            Bitmap B = grey != 2
                ? (grey != 1
                    ? this.getBitmapCB(face, new Gen.getPixelDelegateCB(this.getAlphaScale))
                    : this.getBitmapCB(face, new Gen.getPixelDelegateCB(this.getGreyScale)))
                : (this.ColorsGradientLat.Count <= 0
                    ? this.getBitmapCB(face, new Gen.getPixelDelegateCB(this.getColorScale))
                    : this.getBitmapCB(face, new Gen.getPixelDelegateCB(this.getColorScaleLatCB)));
            this.storeBitmap(fname, B);
            if (fname.Contains(":\\"))
            {
                try
                {
                    B.Save(fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
            else
            {
                try
                {
                    B.Save(Directory.GetCurrentDirectory() + "\\" + fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
        }

        private void saveNormalCB(int face, string fname, float bump)
        {
            Bitmap normal = this.getNormal(face, bump);
            this.storeBitmap(fname, normal);
            if (fname.Contains(":\\"))
            {
                try
                {
                    normal.Save(fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
            else
            {
                try
                {
                    normal.Save(Directory.GetCurrentDirectory() + "\\" + fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
        }

        public void savePng(string fname, int grey)
        {
            Bitmap B = grey != 2 ? this.getBitmap(grey == 1) : this.getBitmap();
            this.storeBitmap(fname, B);
            if (fname.Contains(":\\"))
            {
                try
                {
                    B.Save(fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
            else
            {
                try
                {
                    B.Save(Directory.GetCurrentDirectory() + "\\" + fname + ".png", ImageFormat.Png);
                }
                catch (Exception ex)
                {
                    this.LogMsg(ref this.log, " savePng : " + ex.Message);
                }
            }
        }

        public void showPng(string fname, int grey)
        {
            Bitmap B = grey != 2 ? this.getBitmap(grey == 1) : this.getBitmap();
            this.storeBitmap(fname, B);
        }

        public void showNormal(string fname, float bump)
        {
            Bitmap normal = this.getNormal(bump);
            this.storeBitmap(fname, normal);
        }

        public void loadPng(string fname)
        {
            Bitmap bitmap;
            try
            {
                bitmap = new Bitmap(Directory.GetCurrentDirectory() + "\\" + fname + ".png");
            }
            catch (Exception ex)
            {
                this.LogMsg(ref this.log, " loadPng : " + ex.Message);
                return;
            }
            this.set2D(bitmap.Width, bitmap.Height);
            if (this.n_dimensions != 2)
                return;
            for (int index1 = 0; index1 < this.wid; ++index1)
            {
                for (int index2 = 0; index2 < this.hei; ++index2)
                {
                    if (index1 < bitmap.Width && index2 < bitmap.Height)
                    {
                        Color pixel = bitmap.GetPixel(index1, index2);
                        int num = (int) ((double) pixel.R * 0.3 + (double) pixel.G * 0.59 + (double) pixel.B * 0.11);
                        this.setV(index1, index2, (float) num);
                    }
                }
            }
        }

        private void testSpiral2(int n, int centerX, int centerY, double a, double b, int cicles)
        {
            for (int index = 0; index < n; ++index)
            {
                int[] numArray1 = this.spiralDistr(centerX, centerY, a, b, cicles, 2.0 * Math.PI / 3.0, 0.1);
                int[] numArray2 = this.spiralDistr(centerX, centerY, a, b, cicles, 4.0 * Math.PI / 3.0, 0.1);
                int[] numArray3 = this.spiralDistr(centerX, centerY, a, b, cicles, 2.0 * Math.PI, 0.1);
                this.addStar(numArray1[0], numArray1[1], 5.0, 2);
                this.addStar(numArray2[0], numArray2[1], 5.0, 2);
                this.addStar(numArray3[0], numArray3[1], 5.0, 2);
            }
        }

        private void testSpiral4(int n, int centerX, int centerY, double a, double b, int cicles)
        {
            for (int index = 0; index < n; ++index)
            {
                int[] numArray = this.spiralDistr(centerX, centerY, a, b, cicles, 0.0, 0.01, 0.02, 0.2);
                this.SingleF_Noise(numArray[0], numArray[1], 30, 30, 0.5, 5, 3f, this.ff);
            }
        }

        public void Spiral_Noise(int N, int centerX, int centerY, double a, double b, int cicles, int spiralHarms,
            int boxSizeW, int boxSizeH, double reductionFactor, int deepness, float delta)
        {
            if (boxSizeW > this.wid)
                boxSizeW = this.wid;
            if (boxSizeH > this.hei)
                boxSizeH = this.wid;
            if (spiralHarms == 0)
                spiralHarms = 1;
            double num1 = 2.0 * Math.PI / (double) spiralHarms;
            double startAngle = 0.0;
            for (int index1 = 1; index1 <= spiralHarms; ++index1)
            {
                for (int index2 = 0; index2 < N / index1; ++index2)
                {
                    int[] numArray = this.spiralDistr(centerX, centerY, a, b, cicles, startAngle, 0.1);
                    int num2 = (int) ((double) boxSizeW * ((double) numArray[3] / (double) numArray[4]));
                    int num3 = (int) ((double) boxSizeH * ((double) numArray[3] / (double) numArray[4]));
                    int boxSizeW1 = boxSizeW - num2;
                    int boxSizeH1 = boxSizeH - num3;
                    this.SingleF_Noise(numArray[0], numArray[1], boxSizeW1, boxSizeH1, reductionFactor, deepness, delta,
                        this.ff);
                    startAngle += num1;
                }
            }
        }

        public void Spiral_Galaxy(int N, int centerX, int centerY, double a, double b, int cicles, int spiralHarms,
            int fromP, int toP, int fromR, int toR)
        {
            if (spiralHarms == 0)
                spiralHarms = 1;
            double num = 2.0 * Math.PI / (double) spiralHarms;
            double startAngle = 0.0;
            for (int i = 1; i <= spiralHarms; ++i)
            {
                for (int j = 0; j < N / i; ++j)
                {
                    double power = r.Next(fromP, toP);
                    int radius = r.Next(fromR, toR);

                    int[] numArray = spiralDistr(centerX, centerY, a, b, cicles, startAngle, 0.05);
                    addStar(numArray[0], numArray[1], power, radius);
                    startAngle += num;
                }
            }
        }

        private int[] spiralDistr(int centerX, int centerY, double a, double b, int cicles, double startAngle,
            double gaussianMean, double gaussianVariance, double perturbation)
        {
            int[] numArray = new int[4];
            double num1 = Math.PI * (double) cicles * 2.0;
            double num2 = this.nextGaussian(gaussianMean, gaussianVariance) * num1;
            double num3 = a + b * num2;
            double num4 = num3 + this.r.NextDouble() * (num3 * perturbation);
            double num5 = startAngle + num2;
            int num6 = (int) (num4 * Math.Cos(num5));
            int num7 = (int) (num4 * Math.Sin(num5));
            numArray[0] = this.seamlessCoord(centerX + num6, this.wid, this.seamlessX);
            numArray[1] = this.seamlessCoord(centerY + num7, this.hei, this.seamlessY);
            numArray[2] = (int) num4;
            numArray[3] = (int) num2;
            return numArray;
        }

        private int[] spiralDistr(int centerX, int centerY, double a, double b, int cicles, double startAngle,
            double perturbation)
        {
            int[] numArray = new int[5];
            double num1 = Math.PI * (double) cicles * 2.0;
            double num2 = this.r.NextDouble() * num1;
            double num3 = a + b * num2;
            double num4 = num3 + this.r.NextDouble() * (num3 * perturbation);
            double num5 = startAngle + num2;
            int num6 = (int) (num4 * Math.Cos(num5));
            int num7 = (int) (num4 * Math.Sin(num5));
            numArray[0] = this.seamlessCoord(centerX + num6, this.wid, this.seamlessX);
            numArray[1] = this.seamlessCoord(centerY + num7, this.hei, this.seamlessY);
            numArray[2] = (int) num4;
            numArray[3] = (int) num2;
            numArray[4] = (int) num1;
            return numArray;
        }

        private void testSpiral3(int n, int centerX, int centerY, double a, double b, int cicles)
        {
            for (int index = 0; index < n; ++index)
            {
                int[] numArray = this.spiralDistr(centerX, centerY, a, b, cicles, 0.0, 0.01, 0.02, 0.1);
                this.addStar(numArray[0], numArray[1], (double) numArray[2], 2);
            }
        }

        private delegate float f(float x1, float y1);

        private delegate int flatCoord(int i, int j, int k);

        public delegate Color getPixelDelegate(int i, int j, float lat);

        public delegate Color getPixelDelegateCB(int i, int j, int k, float lat);
    }
}