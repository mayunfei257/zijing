package com.zijing.waigua.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zijing.BaseControl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DimensionLongjie {

	public static class WorldProviderMod extends WorldProvider {

		@Override
		public void init() {
			this.biomeProvider = new BiomeProviderCustom(this.world.getSeed(), world.getWorldInfo().getTerrainType());
		}

		public DimensionType getDimensionType() {
			return BaseControl.dtype;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public Vec3d getFogColor(float par1, float par2) {
			return new Vec3d(1.0D, 1.0D, 1.0D);
		}

		@Override
		public IChunkGenerator createChunkGenerator() {
			return new ChunkProviderModded(this.world, this.world.getSeed() - 5122);
		}

		@Override
		public boolean isSurfaceWorld() {
			return false;
		}

		@Override
		public boolean canCoordinateBeSpawn(int par1, int par2) {
			return false;
		}

		@Override
		public boolean canRespawnHere() {
			return false;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public boolean doesXZShowFog(int par1, int par2) {
			return false;
		}

		@Override
		protected void generateLightBrightnessTable() {
			float f = 0.5F;
			for (int i = 0; i <= 15; ++i) {
				float f1 = 1.0F - (float) i / 15.0F;
				this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
			}
		}

	}


	public static class ChunkProviderModded implements IChunkGenerator {
		private Random rand;
		protected static final IBlockState END_STONE = Blocks.DIAMOND_BLOCK.getDefaultState();
		protected static final IBlockState END_STONE2 = Blocks.EMERALD_BLOCK.getDefaultState();
		protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
		private NoiseGeneratorOctaves field_185969_i;
		private NoiseGeneratorOctaves field_185970_j;
		private NoiseGeneratorOctaves field_185971_k;
		public NoiseGeneratorOctaves noiseGen5;
		public NoiseGeneratorOctaves noiseGen6;
		private final World world;
		private NoiseGeneratorSimplex islandNoise;
		private double[] field_185974_p;
		private Biome[] biomesForGeneration;
		double[] field_185966_e;
		double[] field_185967_f;
		double[] field_185968_g;
		private final WorldGenEndIsland field_185975_r = new WorldGenEndIsland();
		private int chunkX = 0;
		private int chunkZ = 0;

		public ChunkProviderModded(World worldIn, long seed) {
			this.world = worldIn;
			this.rand = new Random(seed);
			this.field_185969_i = new NoiseGeneratorOctaves(this.rand, 16);
			this.field_185970_j = new NoiseGeneratorOctaves(this.rand, 16);
			this.field_185971_k = new NoiseGeneratorOctaves(this.rand, 8);
			this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
			this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
			this.islandNoise = new NoiseGeneratorSimplex(this.rand);
			net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextEnd ctx = new net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextEnd(
					field_185969_i, field_185970_j, field_185971_k, noiseGen5, noiseGen6, islandNoise);
			ctx = net.minecraftforge.event.terraingen.TerrainGen.getModdedNoiseGenerators(worldIn, this.rand, ctx);
			this.field_185969_i = ctx.getLPerlin1();
			this.field_185970_j = ctx.getLPerlin2();
			this.field_185971_k = ctx.getPerlin();
			this.noiseGen5 = ctx.getDepth();
			this.noiseGen6 = ctx.getScale();
			this.islandNoise = ctx.getIsland();
		}

		@Override
		public Chunk generateChunk(int x, int z) {
			this.chunkX = x;
			this.chunkZ = z;
			this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
			ChunkPrimer chunkprimer = new ChunkPrimer();
			this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, x * 16, z * 16, 16, 16);
			this.setBlocksInChunk(x, z, chunkprimer);
			this.func_185962_a(chunkprimer);

			Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
			byte[] abyte = chunk.getBiomeArray();

			for (int i = 0; i < abyte.length; ++i) {
				abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
			}

			chunk.generateSkylightMap();
			return chunk;
		}

		@Override
		public void populate(int x, int z) {
			BlockFalling.fallInstantly = true;
			net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, false);
			BlockPos blockpos = new BlockPos(x * 16, 0, z * 16);

			this.world.getBiome(blockpos.add(16, 0, 16)).decorate(this.world, this.world.rand, blockpos);
			long i = (long) x * (long) x + (long) z * (long) z;

			if (i > 4096L) {
				float f = this.func_185960_a(x, z, 1, 1);

				if (f < -20.0F && this.rand.nextInt(14) == 0) {
					this.field_185975_r.generate(this.world, this.rand,
							blockpos.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));

					if (this.rand.nextInt(4) == 0) {
						this.field_185975_r.generate(this.world, this.rand,
								blockpos.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
					}
				}

			}

			int var4 = x * 16;
			int var5 = z * 16;

			net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, false);
			BlockFalling.fallInstantly = false;
		}

		@Override
		public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
			Biome biome = this.world.getBiome(pos);
			return biome.getSpawnableList(creatureType);
		}

		@Override
		public void recreateStructures(Chunk chunkIn, int x, int z) {
		}

		@Override
		public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
			return false;
		}

		@Override
		public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
			return null;
		}

		@Override
		public boolean generateStructures(Chunk chunkIn, int x, int z) {
			return false;
		}

		private double[] func_185963_a(double[] p_185963_1_, int p_185963_2_, int p_185963_3_, int p_185963_4_, int p_185963_5_, int p_185963_6_,
				int p_185963_7_) {
			net.minecraftforge.event.terraingen.ChunkGeneratorEvent.InitNoiseField event = new net.minecraftforge.event.terraingen.ChunkGeneratorEvent.InitNoiseField(
					this, p_185963_1_, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
			if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
				return event.getNoisefield();

			if (p_185963_1_ == null) {
				p_185963_1_ = new double[p_185963_5_ * p_185963_6_ * p_185963_7_];
			}

			double d0 = 684.412D;
			double d1 = 684.412D;
			d0 = d0 * 2.0D;
			this.field_185966_e = this.field_185971_k.generateNoiseOctaves(this.field_185966_e, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_,
					p_185963_6_, p_185963_7_, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
			this.field_185967_f = this.field_185969_i.generateNoiseOctaves(this.field_185967_f, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_,
					p_185963_6_, p_185963_7_, d0, d1, d0);
			this.field_185968_g = this.field_185970_j.generateNoiseOctaves(this.field_185968_g, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_,
					p_185963_6_, p_185963_7_, d0, d1, d0);
			int i = p_185963_2_ / 2;
			int j = p_185963_4_ / 2;
			int k = 0;

			for (int l = 0; l < p_185963_5_; ++l) {
				for (int i1 = 0; i1 < p_185963_7_; ++i1) {
					float f = this.func_185960_a(i, j, l, i1);

					for (int j1 = 0; j1 < p_185963_6_; ++j1) {
						double d2 = 0.0D;
						double d3 = this.field_185967_f[k] / 512.0D;
						double d4 = this.field_185968_g[k] / 512.0D;
						double d5 = (this.field_185966_e[k] / 10.0D + 1.0D) / 2.0D;

						if (d5 < 0.0D) {
							d2 = d3;
						} else if (d5 > 1.0D) {
							d2 = d4;
						} else {
							d2 = d3 + (d4 - d3) * d5;
						}

						d2 = d2 - 8.0D;
						d2 = d2 + (double) f;
						int k1 = 2;

						if (j1 > p_185963_6_ / 2 - k1) {
							double d6 = (double) ((float) (j1 - (p_185963_6_ / 2 - k1)) / 64.0F);
							d6 = MathHelper.clamp(d6, 0.0D, 1.0D);
							d2 = d2 * (1.0D - d6) + -3000.0D * d6;
						}

						k1 = 8;

						if (j1 < k1) {
							double d7 = (double) ((float) (k1 - j1) / ((float) k1 - 1.0F));
							d2 = d2 * (1.0D - d7) + -30.0D * d7;
						}

						p_185963_1_[k] = d2;
						++k;
					}
				}
			}

			return p_185963_1_;
		}

		private void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
			int i = 2;
			int j = i + 1;
			int k = 33;
			int l = i + 1;
			this.field_185974_p = this.func_185963_a(this.field_185974_p, x * i, 0, z * i, j, k, l);

			for (int i1 = 0; i1 < i; ++i1) {
				for (int j1 = 0; j1 < i; ++j1) {
					for (int k1 = 0; k1 < 32; ++k1) {
						double d0 = 0.25D;
						double d1 = this.field_185974_p[((i1 + 0) * l + j1 + 0) * k + k1 + 0];
						double d2 = this.field_185974_p[((i1 + 0) * l + j1 + 1) * k + k1 + 0];
						double d3 = this.field_185974_p[((i1 + 1) * l + j1 + 0) * k + k1 + 0];
						double d4 = this.field_185974_p[((i1 + 1) * l + j1 + 1) * k + k1 + 0];
						double d5 = (this.field_185974_p[((i1 + 0) * l + j1 + 0) * k + k1 + 1] - d1) * d0;
						double d6 = (this.field_185974_p[((i1 + 0) * l + j1 + 1) * k + k1 + 1] - d2) * d0;
						double d7 = (this.field_185974_p[((i1 + 1) * l + j1 + 0) * k + k1 + 1] - d3) * d0;
						double d8 = (this.field_185974_p[((i1 + 1) * l + j1 + 1) * k + k1 + 1] - d4) * d0;

						for (int l1 = 0; l1 < 4; ++l1) {
							double d9 = 0.125D;
							double d10 = d1;
							double d11 = d2;
							double d12 = (d3 - d1) * d9;
							double d13 = (d4 - d2) * d9;

							for (int i2 = 0; i2 < 8; ++i2) {
								double d14 = 0.125D;
								double d15 = d10;
								double d16 = (d11 - d10) * d14;

								for (int j2 = 0; j2 < 8; ++j2) {
									IBlockState iblockstate = AIR;

									if (d15 > 0.0D) {
										iblockstate = END_STONE;
									}

									int k2 = i2 + i1 * 8;
									int l2 = l1 + k1 * 4;
									int i3 = j2 + j1 * 8;
									primer.setBlockState(k2, l2, i3, iblockstate);
									d15 += d16;
								}

								d10 += d12;
								d11 += d13;
							}

							d1 += d5;
							d2 += d6;
							d3 += d7;
							d4 += d8;
						}
					}
				}
			}
		}

		private float func_185960_a(int p_185960_1_, int p_185960_2_, int p_185960_3_, int p_185960_4_) {
			float f = (float) (p_185960_1_ * 2 + p_185960_3_);
			float f1 = (float) (p_185960_2_ * 2 + p_185960_4_);
			float f2 = 100.0F - MathHelper.sqrt(f * f + f1 * f1) * 8.0F;

			if (f2 > 80.0F) {
				f2 = 80.0F;
			}

			if (f2 < -100.0F) {
				f2 = -100.0F;
			}

			for (int i = -12; i <= 12; ++i) {
				for (int j = -12; j <= 12; ++j) {
					long k = (long) (p_185960_1_ + i);
					long l = (long) (p_185960_2_ + j);

					if (k * k + l * l > 4096L && this.islandNoise.getValue((double) k, (double) l) < -0.8999999761581421D) {
						float f3 = (MathHelper.abs((float) k) * 3439.0F + MathHelper.abs((float) l) * 147.0F) % 13.0F + 9.0F;
						f = (float) (p_185960_3_ - i * 2);
						f1 = (float) (p_185960_4_ - j * 2);
						float f4 = 100.0F - MathHelper.sqrt(f * f + f1 * f1) * f3;

						if (f4 > 80.0F) {
							f4 = 80.0F;
						}

						if (f4 < -100.0F) {
							f4 = -100.0F;
						}

						if (f4 > f2) {
							f2 = f4;
						}
					}
				}
			}

			return f2;
		}

		private void func_185962_a(ChunkPrimer primer) {
			if (!net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, this.chunkX, this.chunkZ, primer, this.world))
				return;
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					int k = 1;
					int l = -1;
					IBlockState iblockstate = END_STONE;
					IBlockState iblockstate1 = END_STONE2;

					for (int i1 = 127; i1 >= 0; --i1) {
						IBlockState iblockstate2 = primer.getBlockState(i, i1, j);

						if (iblockstate2.getMaterial() == Material.AIR) {
							l = -1;
						} else if (iblockstate2.getBlock() == Blocks.STONE) {
							if (l == -1) {
								if (k <= 0) {
									iblockstate = AIR;
									iblockstate1 = END_STONE;
								}

								l = k;

								if (i1 >= 0) {
									primer.setBlockState(i, i1, j, iblockstate);
								} else {
									primer.setBlockState(i, i1, j, iblockstate1);
								}
							} else if (l > 0) {
								--l;
								primer.setBlockState(i, i1, j, iblockstate1);
							}
						}
					}
				}
			}
		}

	}

	public static class BiomeProviderCustom extends BiomeProvider {

		private GenLayer genBiomes;
		/** A GenLayer containing the indices into Biome.biomeList[] */
		private GenLayer biomeIndexLayer;
		/** The BiomeCache object for this world. */
		private BiomeCache biomeCache;
		/** A list of biomes that the player can spawn in. */
		private List<Biome> biomesToSpawnIn;

		public BiomeProviderCustom() {
			this.biomeCache = new BiomeCache(this);
			this.biomesToSpawnIn = new ArrayList();
			this.biomesToSpawnIn.addAll(allowedBiomes);
		}

		public BiomeProviderCustom(long seed, WorldType worldType) {
			this();
			GenLayer[] agenlayer = GenLayerFix.makeTheWorld(seed, worldType);
			agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayer);
			this.genBiomes = agenlayer[0];
			this.biomeIndexLayer = agenlayer[1];

		}

		public BiomeProviderCustom(World world) {
			this(world.getSeed(), world.getWorldInfo().getTerrainType());

		}

		/**
		 * Gets the list of valid biomes for the player to spawn in.
		 */
		@Override
		public List<Biome> getBiomesToSpawnIn() {
			return this.biomesToSpawnIn;
		}

		/**
		 * Returns the Biome related to the x, z position on the world.
		 */

		public Biome getBiomeGenerator(BlockPos pos) {
			return this.getBiomeGenerator(pos, (Biome) null);
		}

		public Biome getBiomeGenerator(BlockPos pos, Biome biomeGenBaseIn) {
			return this.biomeCache.getBiome(pos.getX(), pos.getZ(), biomeGenBaseIn);
		}

		/**
		 * Return an adjusted version of a given temperature based on the y
		 * height
		 */
		@Override
		@SideOnly(Side.CLIENT)
		public float getTemperatureAtHeight(float par1, int par2) {
			return par1;
		}

		/**
		 * Returns an array of biomes for the location input.
		 */
		@Override
		public Biome[] getBiomesForGeneration(Biome[] par1ArrayOfBiome, int par2, int par3, int par4, int par5) {
			IntCache.resetIntCache();

			if (par1ArrayOfBiome == null || par1ArrayOfBiome.length < par4 * par5) {
				par1ArrayOfBiome = new Biome[par4 * par5];
			}

			int[] aint = this.genBiomes.getInts(par2, par3, par4, par5);

			try {
				for (int i = 0; i < par4 * par5; ++i) {
					par1ArrayOfBiome[i] = Biome.getBiome(aint[i]);
				}

				return par1ArrayOfBiome;
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
				crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(par1ArrayOfBiome.length));
				crashreportcategory.addCrashSection("x", Integer.valueOf(par2));
				crashreportcategory.addCrashSection("z", Integer.valueOf(par3));
				crashreportcategory.addCrashSection("w", Integer.valueOf(par4));
				crashreportcategory.addCrashSection("h", Integer.valueOf(par5));
				throw new ReportedException(crashreport);
			}
		}

		/**
		 * Returns biomes to use for the blocks and loads the other data like
		 * temperature and humidity onto the WorldChunkManager Args:
		 * oldBiomeList, x, z, width, depth
		 */
		@Override
		public Biome[] getBiomes(Biome[] oldBiomeList, int x, int z, int width, int depth) {
			return this.getBiomes(oldBiomeList, x, z, width, depth, true);
		}

		/**
		 * Return a list of biomes for the specified blocks. Args: listToReuse,
		 * x, y, width, length, cacheFlag (if false, don't check biomeCache to
		 * avoid infinite loop in BiomeCacheBlock)
		 */
		@Override
		public Biome[] getBiomes(Biome[] listToReuse, int x, int y, int width, int length, boolean cacheFlag) {
			IntCache.resetIntCache();

			if (listToReuse == null || listToReuse.length < width * length) {
				listToReuse = new Biome[width * length];
			}

			if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (y & 15) == 0) {
				Biome[] aBiome1 = this.biomeCache.getCachedBiomes(x, y);
				System.arraycopy(aBiome1, 0, listToReuse, 0, width * length);
				return listToReuse;
			} else {
				int[] aint = this.biomeIndexLayer.getInts(x, y, width, length);

				for (int i = 0; i < width * length; ++i) {
					listToReuse[i] = Biome.getBiome(aint[i]);
				}
				return listToReuse;
			}
		}

		/**
		 * checks given Chunk's Biomes against List of allowed ones
		 */
		@Override
		public boolean areBiomesViable(int x, int y, int z, List par4List) {
			IntCache.resetIntCache();
			int l = x - z >> 2;
			int i1 = y - z >> 2;
			int j1 = x + z >> 2;
			int k1 = y + z >> 2;
			int l1 = j1 - l + 1;
			int i2 = k1 - i1 + 1;
			int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

			try {
				for (int j2 = 0; j2 < l1 * i2; ++j2) {
					Biome biome = Biome.getBiome(aint[j2]);

					if (!par4List.contains(biome)) {
						return false;
					}
				}

				return true;
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
				crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
				crashreportcategory.addCrashSection("x", Integer.valueOf(x));
				crashreportcategory.addCrashSection("z", Integer.valueOf(y));
				crashreportcategory.addCrashSection("radius", Integer.valueOf(z));
				crashreportcategory.addCrashSection("allowed", par4List);
				throw new ReportedException(crashreport);
			}
		}

		/**
		 * Finds a valid position within a range, that is in one of the listed
		 * biomes. Searches {par1,par2} +-par3 blocks. Strongly favors positive
		 * y positions.
		 */
		@Override
		public BlockPos findBiomePosition(int x, int z, int range, List biomes, Random random) {
			IntCache.resetIntCache();
			int l = x - range >> 2;
			int i1 = z - range >> 2;
			int j1 = x + range >> 2;
			int k1 = z + range >> 2;
			int l1 = j1 - l + 1;
			int i2 = k1 - i1 + 1;
			int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
			BlockPos blockpos = null;
			int j2 = 0;

			for (int k2 = 0; k2 < l1 * i2; ++k2) {
				int l2 = l + k2 % l1 << 2;
				int i3 = i1 + k2 / l1 << 2;
				Biome biome = Biome.getBiome(aint[k2]);

				if (biomes.contains(biome) && (blockpos == null || random.nextInt(j2 + 1) == 0)) {
					blockpos = new BlockPos(l2, 0, i3);
					++j2;
				}
			}

			return blockpos;
		}

		/**
		 * Calls the WorldChunkManager's biomeCache.cleanupCache()
		 */
		@Override
		public void cleanupCache() {
			this.biomeCache.cleanupCache();
		}
	}

	public static class GenLayerFix extends GenLayer {

		public GenLayerFix(long seed) {
			super(seed);
		}

		public static GenLayer[] makeTheWorld(long seed, WorldType type) {
			GenLayer biomes = new GenLayerBiomesCustom(1L);
			biomes = new GenLayerZoom(1000L, biomes);
			biomes = new GenLayerZoom(1001L, biomes);
			biomes = new GenLayerZoom(1002L, biomes);
			biomes = new GenLayerZoom(1003L, biomes);
			biomes = new GenLayerZoom(1004L, biomes);
			biomes = new GenLayerZoom(1005L, biomes);
			GenLayer genlayervoronoizoom = new GenLayerVoronoiZoom(10L, biomes);
			biomes.initWorldGenSeed(seed);
			genlayervoronoizoom.initWorldGenSeed(seed);
			return new GenLayer[]{biomes, genlayervoronoizoom};
		}

		@Override
		public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
			return null;
		}
	}

	public static class GenLayerBiomesCustom extends GenLayer {

		protected Biome[] allowedBiomes = {Biome.REGISTRY.getObject(new ResourceLocation("plains")),};

		public GenLayerBiomesCustom(long seed) {
			super(seed);
		}

		public GenLayerBiomesCustom(long seed, GenLayer genlayer) {
			super(seed);
			this.parent = genlayer;
		}

		@Override
		public int[] getInts(int x, int z, int width, int depth) {
			int[] dest = IntCache.getIntCache(width * depth);
			for (int dz = 0; dz < depth; dz++) {
				for (int dx = 0; dx < width; dx++) {
					this.initChunkSeed(dx + x, dz + z);
					dest[(dx + dz * width)] = Biome.getIdForBiome(this.allowedBiomes[nextInt(this.allowedBiomes.length)]);
				}
			}
			return dest;
		}
	}

	
	
	
		
	// util
	public static Block getBlock(IBlockAccess world, int i, int j, int k) {
		return world.getBlockState(new BlockPos(i, j, k)).getBlock();
	}
}
