require 'json'
require 'date'

class Migrator

  def initialize(migration_name, split_size)
    @migration_name = migration_name
    @split_size = split_size          # In bytes
    @file_num = 1
    @datetime = DateTime.now.strftime('%Y%m%d%H%M')
    @file = File.open(file_name, 'w')
  end

  def append(line)
    if @file.size > @split_size
      @file.close
      @file_num += 1
      @file = File.open(file_name, 'w')
    end
    @file.puts line
    @file.flush

    nil
  end

  def finish
    @file.close
    @file_num
  end

  # Escape quotes Postgres-style (ie. ' => '') if str is not nil. Otherwise return 'NULL'.
  def self.postgres_string(str)
    str ? "'#{str.gsub("'", "''")}'" : 'NULL'
  end

  # Convert epoch timestamps (in milliseconds) to ISO-8601 date (ie. YYYY-MM-DD). If nil, return 'NULL'
  def self.postgres_date(timestamp)
    timestamp ? "'#{DateTime.strptime("#{timestamp/1000}", '%s').to_date.iso8601}'" : 'NULL'
  end

  private

  def file_name
    "V1_#{@datetime}_#{suffix}__#{@migration_name}_#{suffix}.sql"
  end

  def suffix
    '%03i' % @file_num
  end
end
