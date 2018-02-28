/**
 * 
 */
package com.aionemu.packetsamurai.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;


import com.aionemu.packetsamurai.gui.PacketTableRenderer.TooltipTable;
import com.aionemu.packetsamurai.gui.images.IconsTable;
import com.aionemu.packetsamurai.protocol.protocoltree.PacketFamilly.packetDirection;
import com.aionemu.packetsamurai.session.DataPacket;

import javolution.util.FastTable;

/**
 * @author Ulysses R. Ribeiro
 *
 */
@SuppressWarnings("serial") 
class PacketTableModel extends AbstractTableModel implements TooltipTable
{
    private static final String[] columnNames =
    {
        "S/C",
        "Opcode",
        "Time",
        "Length",
        "Name"
    };
    
    private FastTable<Object[]> _currentTable;

    private List<DataPacket> currentPackets;
    
    public PacketTableModel()
    {
    }
    
    public void reinit(int size)
    {
    	_currentTable = new FastTable<Object[]>();
    	currentPackets = new ArrayList<DataPacket>();
    }

    @Override
    public int getColumnCount() 
    {
        return columnNames.length;
    }

    @Override
    public int getRowCount() 
    {
        return (_currentTable == null ? 0 : _currentTable.size());
    }

    @Override
    public String getColumnName(int col)
    {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) 
    {
    	Object[] tableRow = _currentTable.get(row);
    	if (tableRow != null)
    		return tableRow[col];
    	return "";
    }

    public boolean isCellEditable(int row, int col) 
    {
        	return false;
    }
    
    public void addRow(DataPacket packet, long startTime)
    {
        ImageIcon icon = null;
        if(packet.getDirection() == packetDirection.clientPacket)
        {
            if(packet.hasError())
            {
                icon = IconsTable.ICON_FROM_CLIENT_ERROR;
            }
            else if(packet.hasWarning())
            {
                icon = IconsTable.ICON_FROM_CLIENT_WARNING;
            }
            else
            {
                icon = IconsTable.ICON_FROM_CLIENT;
            }
        }
        else
        {
            if (packet.hasError())
            {
                icon = IconsTable.ICON_FROM_SERVER_ERROR;
            }
            else if (packet.hasWarning())
            {
                icon = IconsTable.ICON_FROM_SERVER_WARNING;
            }
            else
            {
                icon = IconsTable.ICON_FROM_SERVER;
            }
        }
        String opcode = null;
        if(packet.getPacketFormat() != null)
        {
            opcode = packet.getPacketFormat().getOpcodeStr();
        }
        else
        {
            opcode = "-";
        }
        
        String time = "+"+(packet.getTimeStamp()-startTime)+" ms";
        String toolTip = null;
        if (packet.hasError() || packet.hasWarning())
        {
            String color = (packet.hasError() ? "red" : "gray");
            toolTip = "<br><font color=\""+color+"\">"+packet.getErrorMessage()+"</font></html>";
        }
        
        Object[] temp = { new JLabel(icon), opcode, time, String.valueOf(packet.getSize()), packet.getName(), toolTip, false};
        _currentTable.add(temp);
        currentPackets.add(packet);
    }

    @Override
    public String getToolTip(int row, int col)
    {
        String toolTip = "<html>Packet: "+row;
        Object msg = _currentTable.get(row)[5];
        if (msg != null)
        {
            toolTip += msg;
        }
        return toolTip;
    }

    public void setIsMarked(int row, boolean val)
    {
        _currentTable.get(row)[6] = val;
    }

    @Override
    public boolean getIsMarked(int row)
    {
        return (Boolean) _currentTable.get(row)[6];
    }

	public DataPacket getPacket(int index)
	{
		return currentPackets.get(index);
	}

	public List<DataPacket> getAllPackets()
	{
		return Collections.unmodifiableList(currentPackets);
	}
}