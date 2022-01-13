import aiohttp
import discord
import sys

from discord.ext import tasks
from pycoingecko import CoinGeckoAPI


DISCORD_TOKEN = sys.argv[1]
client = discord.Client()
cg = CoinGeckoAPI()


@client.event
async def on_ready():
    await update_price()


@tasks.loop(hours=1)
async def task_update_price():
    await update_price()


async def update_price():
    price_per_stx = await get_price()
    stx_price = cg.get_price(ids='blockstack', vs_currencies='usd')['blockstack']['usd']

    price_in_usd = stx_price / price_per_stx
    await client.change_presence(
        activity=discord.Activity(type=discord.ActivityType.watching, name=f'${round(price_in_usd, 7)}')
    )


async def get_price():
    async with aiohttp.ClientSession() as session:
        async with session.get('https://arkadiko-api.herokuapp.com/api/v1/pools/6/prices') as resp:
            price_list = (await resp.json())['prices']
            latest_price = price_list[-1][1]

            return latest_price


task_update_price.start()
client.run(DISCORD_TOKEN)
