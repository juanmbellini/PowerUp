require 'open-uri'
require 'certified'   # Run `gem install certified` for this to work

class Query
  API_URL = 'https://api-2445582011268.apicast.io'.freeze
  KEY = '94013c0f817d9aef069c6a8b43b2b2d9'.freeze # TODO: Move this to passwords.properties? Also invalidate this key

  attr_reader :url, :headers, :response

  def initialize(url, headers)
    @url = url
    @headers = headers
  end

  def fire
    @response = open(@url, @headers)
  end

  def get_all(&block)
    # Get scroll URL in first page
    first_page = fire
    block.call(first_page.read)

    @url = "#{API_URL}#{scroll_url}"
    while true do
      begin
        page = fire.read
        block.call(page)
      rescue Exception => e
        puts "Exception: #{e}"
        puts "Assuming this means we're done. Exiting"
        return
      end
    end
  end

  def total
    @response.meta['x-count']
  end

  private

  def scroll_url
    @response.meta['x-next-page']
  end
end
